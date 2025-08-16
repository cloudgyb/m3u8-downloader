package com.github.cloudgyb.m3u8downloader.download;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.entity.MediaSegmentEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.domain.service.MediaSegmentService;
import com.github.cloudgyb.m3u8downloader.event.DownloadRateChangeEvent;
import com.github.cloudgyb.m3u8downloader.event.DownloadTaskStatusChangeEvent;
import com.github.cloudgyb.m3u8downloader.event.DownloadTaskStatusChangeEventNotifier;
import com.github.cloudgyb.m3u8downloader.m3u8.M3U8Parser;
import com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskStatus;
import com.github.cloudgyb.m3u8downloader.model.ProgressAndStatus;
import com.github.cloudgyb.m3u8downloader.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 任务下载线程
 *
 * @author cloudgyb
 * @since 2025/06/30 16:50
 */
public class TaskDownloadThread extends Thread {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ThreadPoolExecutor threadPool;
    private final M3U8Parser m3U8Parser = new M3U8Parser();
    private final DownloadTaskEntity task;
    private final MediaSegmentService mediaSegmentService = MediaSegmentService.getInstance();
    private final DownloadTaskService downloadTaskService = DownloadTaskService.getInstance();
    private final DownloadTaskStatusChangeEventNotifier eventNotifier = DownloadTaskStatusChangeEventNotifier.INSTANCE;
    private final AtomicBoolean isStopped = new AtomicBoolean(true);
    private final TaskDownloadThreadManager taskDownloadThreadManager = TaskDownloadThreadManager.getInstance();
    /**
     * 下载字节计数器，用于计算速率
     */
    private final AtomicLong bytesCounter = new AtomicLong();

    public TaskDownloadThread(DownloadTaskEntity task, ThreadPoolExecutor threadPool) {
        this.task = task;
        this.threadPool = threadPool;
        setName("TaskDownloadManageThread " + task.getId());
    }

    @Override
    public void run() {
        isStopped.set(false);
        try {
            long beginTime = System.currentTimeMillis();
            String stage = task.getStage();
            DownloadTaskStageEnum downloadTaskStageEnum = DownloadTaskStageEnum.valueOf(stage);
            // 如果处于 NEW 阶段，则先解析 m3u8 索引文件
            if (DownloadTaskStageEnum.NEW == downloadTaskStageEnum) {
                m3u8IndexParse();
            }
            stage = task.getStage();
            downloadTaskStageEnum = DownloadTaskStageEnum.valueOf(stage);
            // 如果还在 NEW 阶段，说明 m3u8 解析失败了，退出
            if (DownloadTaskStageEnum.NEW == downloadTaskStageEnum) {
                return;
            }
            if (isStopOrThreadInterrupted()) {
                return;
            }
            // 如果 m3u8 索引文件解析完成则开始（或继续）进行片段下载
            if (DownloadTaskStageEnum.M3U8_PARSED == downloadTaskStageEnum) {
                downloadMediaSegments(task);
            }
            stage = task.getStage();
            downloadTaskStageEnum = DownloadTaskStageEnum.valueOf(stage);
            // 如果还在 M3U8_PARSED 阶段，则下载失败了，退出
            if (DownloadTaskStageEnum.M3U8_PARSED == downloadTaskStageEnum) {
                return;
            }
            if (isStopOrThreadInterrupted()) {
                return;
            }
            // 如果处于 DOWNLOAD_FINISHED 阶段，则开始媒体片段合并
            if (DownloadTaskStageEnum.DOWNLOAD_FINISHED == downloadTaskStageEnum) {
                mergerMediaSegment(task);
            }
            stage = task.getStage();
            downloadTaskStageEnum = DownloadTaskStageEnum.valueOf(stage);
            // 如果还处于 DOWNLOAD_FINISHED 则合并失败，退出
            if (DownloadTaskStageEnum.DOWNLOAD_FINISHED == downloadTaskStageEnum) {
                return;
            }
            // 成功（完成）
            task.setStage(DownloadTaskStageEnum.FINISHED.name());
            task.setStatus(DownloadTaskStatusEnum.FINISHED.name());
            task.setFinishedTime(new Date());
            long endTime = System.currentTimeMillis();
            task.setDownloadDuration(task.getDownloadDuration() + (endTime - beginTime));
            downloadTaskService.updateById(task);
            publishStatus(DownloadTaskStatusEnum.FINISHED, 100.0, DownloadTaskStageEnum.FINISHED);
        } finally {
            taskDownloadThreadManager.removeFuture(task.getId());
            isStopped.set(true);
            @SuppressWarnings("unused")
            boolean isInterrupted = Thread.interrupted(); // 清除线程中断状态
            logger.info("任务(ID:{})下载线程终止退出！", task.getId());
        }
    }

    private boolean isStopOrThreadInterrupted() {
        return isStopped.get() || Thread.currentThread().isInterrupted();
    }

    private void mergerMediaSegment(DownloadTaskEntity task) {
        task.setStatus(DownloadTaskStatusEnum.SEGMENT_MERGING.name());
        downloadTaskService.updateById(task);
        publishStatus(DownloadTaskStatusEnum.SEGMENT_MERGING, 100.0, DownloadTaskStageEnum.DOWNLOAD_FINISHED);
        Integer tid = task.getId();
        try {
            // 合并媒体片段
            List<MediaSegmentEntity> list = mediaSegmentService.getByTaskId(tid, true, -1);
            List<String> fileSegments = list.stream().map(MediaSegmentEntity::getFilePath).collect(Collectors.toList());
            String downloadDir = ApplicationStore.getSystemConfig().getDownloadDir();
            String saveFilename = task.getSaveFilename();
            if (saveFilename == null || saveFilename.trim().isEmpty()) {
                saveFilename = String.valueOf(tid);
            }
            saveFilename = saveFilename.replace(" ", "") + ".mp4"; // windows 打开文件时，文件名不能有空格
            String targetFilePath = downloadDir + File.separator + saveFilename;
            FfmpegUtil.mergeTS(fileSegments, targetFilePath, true);
            task.setStage(DownloadTaskStageEnum.SEGMENT_MERGED.name());
            task.setStatus(DownloadTaskStatusEnum.SEGMENT_MERGED.name());
            task.setFilePath(targetFilePath);
            task.setSaveFilename(saveFilename);
            downloadTaskService.updateById(task);
            publishStatus(DownloadTaskStatusEnum.SEGMENT_MERGED, 0.0, DownloadTaskStageEnum.SEGMENT_MERGED);
        } catch (Exception e) {
            task.setStage(DownloadTaskStageEnum.DOWNLOAD_FINISHED.name());
            task.setStatus(DownloadTaskStatusEnum.SEGMENT_MERGE_FAILED.name());
            downloadTaskService.updateById(task);
            publishStatus(DownloadTaskStatusEnum.SEGMENT_MERGE_FAILED, 0.0,
                    DownloadTaskStageEnum.DOWNLOAD_FINISHED);
        }
    }

    /**
     * 下载媒体片段
     */
    private void downloadMediaSegments(DownloadTaskEntity task) {
        task.setStatus(DownloadTaskStatusEnum.DOWNLOADING.name());
        Future<?> rateUpdateThreadFuture = startDownloadRateUpdateThread();
        downloadTaskService.updateById(task);
        if (isStopOrThreadInterrupted()) {
            rateUpdateThreadFuture.cancel(true);
            return;
        }
        publishStatus(DownloadTaskStatusEnum.DOWNLOADING, getProgress(task), DownloadTaskStageEnum.M3U8_PARSED);
        Integer tid = task.getId();
        try {
            Integer maxThreadCount = task.getMaxThreadCount();
            maxThreadCount = maxThreadCount == 0 ?
                    ApplicationStore.getSystemConfig().getDefaultThreadCount() : maxThreadCount;
            if (logger.isInfoEnabled()) {
                logger.info("使用最大{}个线程去下载任务（ID:{}）", maxThreadCount, tid);
            }
            ArrayList<Future<MediaSegmentEntity>> futures = new ArrayList<>();
            // 分批下载片段
            for (; ; ) {
                futures.clear();
                if (isStopOrThreadInterrupted()) {
                    return;
                }
                List<MediaSegmentEntity> mediaSegmentEntities = mediaSegmentService
                        .getByTaskId(tid, false, maxThreadCount);
                if (mediaSegmentEntities.isEmpty()) // 所有片段都已经下载完成，退出
                    break;
                for (MediaSegmentEntity mediaSegmentEntity : mediaSegmentEntities) {
                    Future<MediaSegmentEntity> future = threadPool.submit(() -> {
                        long staterTime = System.currentTimeMillis();
                        String url = mediaSegmentEntity.getUrl();
                        if (logger.isInfoEnabled()) {
                            logger.info("开始下载任务(ID:{})媒体片段{}", mediaSegmentEntity.getTaskId(), url);
                        }
                        InputStream inputStream = HttpClientUtil.getAsInputStream(url);
                        File tempDir = new File(ApplicationStore.getTmpDir(),
                                "m3u8_" + task.getCreateTime().getTime());
                        FileUtil.ensureDirExist(tempDir);
                        File tempFile = new File(tempDir, mediaSegmentEntity.getId().toString() + ".ts");
                        FileOutputStream fos = new FileOutputStream(tempFile);
                        try {
                            DataStreamUtil.copy(inputStream, fos, true, true, bytesCounter);
                        } catch (IOException e) {
                            logger.warn("下载媒体片段{}失败,Exception: {}", url, e.getMessage());
                            if (e instanceof InterruptedIOException) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("下载媒体片段{}被中断", url);
                                }
                            }
                            boolean delete = tempFile.delete();
                            if (delete) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("删除临时文件{}成功", tempFile.getAbsolutePath());
                                }
                            } else {
                                logger.warn("删除临时文件{}失败", tempFile.getAbsolutePath());
                            }
                            return null;
                        }
                        mediaSegmentEntity.setFinished(true);
                        mediaSegmentEntity.setFilePath(tempFile.getAbsolutePath());
                        long endTime = System.currentTimeMillis();
                        long duration = endTime - staterTime;
                        mediaSegmentEntity.setDownloadDuration(duration);
                        mediaSegmentService.updateById(mediaSegmentEntity);
                        if (logger.isInfoEnabled()) {
                            logger.info("任务(ID:{})媒体片段下载完成{}", mediaSegmentEntity.getTaskId(), url);
                        }
                        return mediaSegmentEntity;
                    });
                    futures.add(future);
                }
                // 等待所有的媒体片段下载完成
                for (Future<MediaSegmentEntity> future : futures) {
                    try {
                        future.get();
                        task.setFinishMediaSegment(task.getFinishMediaSegment() + 1);
                        task.setStatus(DownloadTaskStatusEnum.DOWNLOADING.name());
                        downloadTaskService.updateById(task);
                        publishStatus(DownloadTaskStatusEnum.DOWNLOADING, getProgress(task),
                                DownloadTaskStageEnum.M3U8_PARSED
                        );
                    } catch (Exception e) {
                        logger.error("等待下载线程池中媒体片段下载完成是发生异常！Exception:{}", e.getClass().getSimpleName());
                        if (e instanceof InterruptedException) { // future.get() 中被中断
                            Thread.currentThread().interrupt(); // 重新设置中断状态，让后续流程能够获取中断状态
                        }
                        if (isStopOrThreadInterrupted()) {
                            futures.forEach(future1 -> future1.cancel(true));
                            futures.clear();
                            logger.info("手动停止。。。");
                            return;
                        }
                    }
                }
            }
            task.setStage(DownloadTaskStageEnum.DOWNLOAD_FINISHED.name());
            task.setStatus(DownloadTaskStatusEnum.DOWNLOAD_FINISHED.name());
            downloadTaskService.updateById(task);
            publishStatus(DownloadTaskStatusEnum.DOWNLOAD_FINISHED, 100.0,
                    DownloadTaskStageEnum.DOWNLOAD_FINISHED);
        } catch (Exception e) {
            task.setStage(DownloadTaskStageEnum.M3U8_PARSED.name()); // 如果下载出现异常，不能将阶段改为下载完成
            task.setStatus(DownloadTaskStatusEnum.DOWNLOAD_FAILED.name());
            downloadTaskService.updateById(task);
            publishStatus(DownloadTaskStatusEnum.DOWNLOAD_FAILED, getProgress(task),
                    DownloadTaskStageEnum.M3U8_PARSED);
        } finally {
            rateUpdateThreadFuture.cancel(true);
        }
    }

    @SuppressWarnings("all")
    private Future<?> startDownloadRateUpdateThread() {
        return threadPool.submit(() -> {
            while (!Thread.currentThread().isInterrupted() && !isStopped.get()) {
                publishDownloadRate(bytesCounter.get(), 1000);
                bytesCounter.set(0L);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                }
            }
        });
    }

    private static double getProgress(DownloadTaskEntity task) {
        return (double) task.getFinishMediaSegment() / task.getTotalMediaSegment();
    }

    /**
     * m3u8 索引文件解析
     */
    private void m3u8IndexParse() {
        int tid = task.getId();
        String url = task.getUrl();
        if (logger.isInfoEnabled()) {
            logger.info("开始解析任务对应的 m3u8 url: {} tid:{}", url, tid);
        }
        task.setStatus(DownloadTaskStatusEnum.M3U8_PARSING.name());
        downloadTaskService.updateById(task);
        publishStatus(DownloadTaskStatusEnum.M3U8_PARSING, null,
                DownloadTaskStageEnum.NEW);
        if (isStopOrThreadInterrupted()) {
            return;
        }
        try {
            Future<List<MediaSegment>> future = threadPool.submit(
                    () -> m3U8Parser.playlistParse(url)
            );
            List<MediaSegment> mediaSegments;
            try {
                mediaSegments = future.get();
            } catch (InterruptedException e) { // 处理中断
                future.cancel(true); // 给解析线程发送一个中断信号
                Thread.currentThread().interrupt(); // 重新设置中断状态，让后续流程能够获取中断状态
                return;
            }
            if (isStopOrThreadInterrupted()) {
                return;
            }
            if (!mediaSegments.isEmpty()) {
                mediaSegmentService.saveAllMediaSegments(tid, mediaSegments);
            } else {
                throw new RuntimeException("解析 m3u8 索引文件失败，文件内容为空！");
            }
            task.setTotalMediaSegment(mediaSegments.size());
            task.setFinishMediaSegment(0);
            task.setStage(DownloadTaskStageEnum.M3U8_PARSED.name());
            task.setStatus(DownloadTaskStatusEnum.M3U8_PARSED.name());
            publishStatus(DownloadTaskStatusEnum.M3U8_PARSED, null, DownloadTaskStageEnum.M3U8_PARSED);
        } catch (Exception e) {
            task.setStage(DownloadTaskStageEnum.NEW.name());
            task.setStatus(DownloadTaskStatusEnum.M3U8_PARSE_FAILED.name());
            publishStatus(DownloadTaskStatusEnum.M3U8_PARSE_FAILED, null, DownloadTaskStageEnum.NEW);
            logger.error("解析任务对应的 m3u8 url: {} tid:{} 失败！", url, tid, e);
        }
        downloadTaskService.updateById(task);
    }

    @SuppressWarnings("all")
    private void publishDownloadRate(long length, long duration) {
        if (this.isStopped.get())
            return;
        double seconds = duration / 1000D;
        long rate = Double.valueOf(length / seconds).longValue();
        String rateHumanReadable = FileUtil.bytesToHumanReadable(rate);
        eventNotifier.publish(new DownloadRateChangeEvent(this.task.getId(), rateHumanReadable));
    }

    private void publishStatus(DownloadTaskStatusEnum statusEnum, Double progress, DownloadTaskStageEnum stageEnum) {
        ProgressAndStatus progressAndStatus = new ProgressAndStatus(statusEnum, progress, stageEnum);
        eventNotifier.publish(
                new DownloadTaskStatusChangeEvent(
                        new DownloadTaskStatus(this.task.getId(), progressAndStatus))
        );
    }

    @SuppressWarnings("unused")
    public void stopDownload() {
        isStopped.set(true);
        // 产生中断，让等待的 Future 退出等待
        this.interrupt();
    }
}
