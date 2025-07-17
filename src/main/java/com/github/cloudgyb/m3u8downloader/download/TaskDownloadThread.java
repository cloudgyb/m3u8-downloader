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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
    private static final int cpuCores = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            cpuCores * 10, cpuCores * 20, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), new TaskDownloadThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());
    private final M3U8Parser m3U8Parser = new M3U8Parser();
    private final DownloadTaskEntity task;
    private final MediaSegmentService mediaSegmentService = MediaSegmentService.getInstance();
    private final DownloadTaskService downloadTaskService = DownloadTaskService.getInstance();
    private final DownloadTaskStatusChangeEventNotifier eventNotifier = DownloadTaskStatusChangeEventNotifier.INSTANCE;
    private final AtomicBoolean isStopped = new AtomicBoolean(true);
    /**
     * 下载字节计数器，用于计算速率
     */
    private final AtomicLong bytesCounter = new AtomicLong();

    static {
        threadPool.allowCoreThreadTimeOut(true);
    }

    public TaskDownloadThread(DownloadTaskEntity task) {
        this.task = task;
        setName("TaskDownloadManageThread " + task.getId());
    }

    @Override
    public void run() {
        isStopped.set(false);
        try {
            long beginTime = System.currentTimeMillis();
            String stage = task.getStage();
            DownloadTaskStageEnum downloadTaskStageEnum = DownloadTaskStageEnum.valueOf(stage);
            boolean needM3u8Parse = isNeedM3u8Parse(downloadTaskStageEnum);
            //先解析 m3u8 ，如果没有解析过
            if (needM3u8Parse) {
                if (isStopped.get()) {
                    return;
                }
                m3u8IndexParse();
            }
            stage = task.getStage();
            downloadTaskStageEnum = DownloadTaskStageEnum.valueOf(stage);
            //如果 m3u8 解析失败了
            if (needM3u8Parse && DownloadTaskStageEnum.M3U8_PARSE_FAILED == downloadTaskStageEnum) {
                return;
            }
            // 如果需要进行片段下载
            if (DownloadTaskStageEnum.M3U8_PARSED == downloadTaskStageEnum ||
                    DownloadTaskStageEnum.DOWNLOADING == downloadTaskStageEnum
                    || DownloadTaskStageEnum.DOWNLOAD_FAILED == downloadTaskStageEnum) {
                if (isStopped.get()) {
                    return;
                }
                downloadMediaSegments(task);
            }
            stage = task.getStage();
            downloadTaskStageEnum = DownloadTaskStageEnum.valueOf(stage);
            // 片段下载是否失败
            if (DownloadTaskStageEnum.DOWNLOAD_FAILED == downloadTaskStageEnum) {
                return;
            }
            // 如果需要媒体片段合并
            if (DownloadTaskStageEnum.DOWNLOAD_FINISHED == downloadTaskStageEnum ||
                    DownloadTaskStageEnum.SEGMENT_MERGING == downloadTaskStageEnum ||
                    DownloadTaskStageEnum.SEGMENT_MERGE_FAILED == downloadTaskStageEnum
            ) {
                if (isStopped.get()) {
                    return;
                }
                mergerMediaSegment(task);
            }
            stage = task.getStage();
            downloadTaskStageEnum = DownloadTaskStageEnum.valueOf(stage);
            // 是否合并失败
            if (DownloadTaskStageEnum.SEGMENT_MERGE_FAILED == downloadTaskStageEnum) {
                return;
            }
            if (isStopped.get()) {
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
            String reason = isStopped.get() ? "手动停止" : "下载完成";
            logger.info("任务(ID:{})下载线程终止退出({})！", task.getId(), reason);
        }
    }

    private void mergerMediaSegment(DownloadTaskEntity task) {
        task.setStage(DownloadTaskStageEnum.SEGMENT_MERGING.name());
        task.setStatus(DownloadTaskStatusEnum.RUNNING.name());
        downloadTaskService.updateById(task);
        publishStatus(DownloadTaskStatusEnum.RUNNING, 100.0, DownloadTaskStageEnum.SEGMENT_MERGING);
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
            task.setStatus(DownloadTaskStatusEnum.RUNNING.name());
            task.setFilePath(targetFilePath);
            task.setSaveFilename(saveFilename);
            downloadTaskService.updateById(task);
            publishStatus(DownloadTaskStatusEnum.RUNNING, 0.0, DownloadTaskStageEnum.SEGMENT_MERGED);
        } catch (Exception e) {
            task.setStage(DownloadTaskStageEnum.SEGMENT_MERGE_FAILED.name());
            task.setStatus(DownloadTaskStatusEnum.STOPPED_ERROR.name());
            downloadTaskService.updateById(task);
            publishStatus(DownloadTaskStatusEnum.STOPPED_ERROR, 0.0, DownloadTaskStageEnum.SEGMENT_MERGE_FAILED);
        }
    }

    private void downloadMediaSegments(DownloadTaskEntity task) {
        task.setStage(DownloadTaskStageEnum.DOWNLOADING.name());
        task.setStatus(DownloadTaskStatusEnum.RUNNING.name());
        startDownloadRateUpdateThread();
        downloadTaskService.updateById(task);
        publishStatus(DownloadTaskStatusEnum.RUNNING,
                getProgress(task),
                DownloadTaskStageEnum.DOWNLOADING);
        Integer tid = task.getId();
        try {
            // 下载片段
            ArrayList<Future<MediaSegmentEntity>> futures = new ArrayList<>();
            Integer maxThreadCount = task.getMaxThreadCount();
            maxThreadCount = maxThreadCount == 0 ?
                    ApplicationStore.getSystemConfig().getDefaultThreadCount() : maxThreadCount;
            logger.info("使用最大{}个线程去下载任务（ID:{}）", maxThreadCount, tid);
            for (; ; ) {
                if (isStopped.get()) {
                    return;
                }
                List<MediaSegmentEntity> mediaSegmentEntities = mediaSegmentService
                        .getByTaskId(tid, false, maxThreadCount);
                if (mediaSegmentEntities.isEmpty())
                    break;
                futures.clear();
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
                        DataStreamUtil.copy(inputStream, fos, true, true, bytesCounter);
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
                        task.setStatus(DownloadTaskStatusEnum.RUNNING.name());
                        downloadTaskService.updateById(task);
                        publishStatus(DownloadTaskStatusEnum.RUNNING,
                                getProgress(task),
                                DownloadTaskStageEnum.DOWNLOADING
                        );
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        if (isStopped.get()) {
                            logger.info("手动停止。。。");
                            return;
                        }
                    }
                }
            }

            task.setStage(DownloadTaskStageEnum.DOWNLOAD_FINISHED.name());
            task.setStatus(DownloadTaskStatusEnum.RUNNING.name());
            downloadTaskService.updateById(task);
            publishStatus(DownloadTaskStatusEnum.RUNNING, 100.0, DownloadTaskStageEnum.DOWNLOAD_FINISHED);
        } catch (Exception e) {
            task.setStage(DownloadTaskStageEnum.DOWNLOAD_FAILED.name());
            task.setStatus(DownloadTaskStatusEnum.STOPPED_ERROR.name());
            downloadTaskService.updateById(task);
            publishStatus(DownloadTaskStatusEnum.STOPPED_ERROR, getProgress(task),
                    DownloadTaskStageEnum.DOWNLOAD_FAILED);
        }
    }

    @SuppressWarnings("all")
    private void startDownloadRateUpdateThread() {
        threadPool.submit(() -> {
            while (!isStopped.get()) {
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

    private boolean isNeedM3u8Parse(DownloadTaskStageEnum downloadTaskStageEnum) {
        return DownloadTaskStageEnum.NEW == downloadTaskStageEnum ||
                DownloadTaskStageEnum.M3U8_PARSING == downloadTaskStageEnum ||
                DownloadTaskStageEnum.M3U8_PARSE_FAILED == downloadTaskStageEnum;
    }

    /**
     * m3u8 索引文件解析
     */
    private void m3u8IndexParse() {
        int tid = task.getId();
        String url = task.getUrl();
        logger.info("开始解析任务对应的 m3u8 url: {} tid:{}", url, tid);
        task.setStage(DownloadTaskStageEnum.M3U8_PARSING.name());
        task.setStatus(DownloadTaskStatusEnum.RUNNING.name());
        downloadTaskService.updateById(task);
        publishStatus(DownloadTaskStatusEnum.RUNNING, null,
                DownloadTaskStageEnum.M3U8_PARSING);
        try {
            Future<List<MediaSegment>> res = threadPool.submit(
                    () -> m3U8Parser.playlistParse(url)
            );
            List<MediaSegment> mediaSegments = res.get();
            if (!mediaSegments.isEmpty()) {
                mediaSegmentService.saveAllMediaSegments(tid, mediaSegments);
            } else {
                throw new RuntimeException("解析 m3u8 索引文件失败，文件内容为空！");
            }
            task.setTotalMediaSegment(mediaSegments.size());
            task.setFinishMediaSegment(0);
            task.setStage(DownloadTaskStageEnum.M3U8_PARSED.name());
            task.setStatus(DownloadTaskStatusEnum.RUNNING.name());
            publishStatus(DownloadTaskStatusEnum.RUNNING, null, DownloadTaskStageEnum.M3U8_PARSED);
        } catch (Exception e) {
            task.setStage(DownloadTaskStageEnum.M3U8_PARSE_FAILED.name());
            task.setStatus(DownloadTaskStatusEnum.STOPPED_ERROR.name());
            publishStatus(DownloadTaskStatusEnum.STOPPED_ERROR, null, DownloadTaskStageEnum.M3U8_PARSE_FAILED);
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

    public void stopDownload() {
        isStopped.set(true);
        // 产生中断，让等待的 Future 退出等待
        this.interrupt();
    }

    private static class TaskDownloadThreadFactory implements ThreadFactory {
        private final AtomicInteger threadCounter = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("TaskDownloadThread " + threadCounter.getAndIncrement());
            return thread;
        }
    }
}
