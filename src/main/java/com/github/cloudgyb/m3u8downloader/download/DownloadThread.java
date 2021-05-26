package com.github.cloudgyb.m3u8downloader.download;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDao;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.domain.SystemConfig;
import com.github.cloudgyb.m3u8downloader.model.DownloadTask;
import com.github.cloudgyb.m3u8downloader.util.DataStreamUtil;
import com.github.cloudgyb.m3u8downloader.util.HttpClientUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * 下载线程，每一个下载任务对应一个线程
 * 真正的下载是使用下载任务封装类DownloadTask中的线程池
 *
 * @author cloudgyb
 * 2021/5/17 17:09
 */
public class DownloadThread extends Thread {
    private final Logger logger = Logger.getLogger(DownloadThread.class.getSimpleName());
    private final DownloadTaskDao downloadTaskDao;
    private final DownloadTask downloadTask;
    //定时器，用于下载时间计时
    private final Timer timer;
    //下载完成的URL计数，用于计算下载进度
    private final AtomicInteger downloadedCount = new AtomicInteger(0);
    private final SystemConfig systemConfig = ApplicationStore.getSystemConfig();

    public DownloadThread(DownloadTask downloadTask) {
        this.downloadTask = downloadTask;
        this.timer = new Timer();
        this.downloadTaskDao = new DownloadTaskDao();
        this.setName("Download-Thread-" + this.downloadTask.getId());
    }

    @Override
    public void run() {
        //每次开始之前重置计数器
        this.downloadedCount.set(0);
        final String downUrl = this.downloadTask.getUrl();
        logger.info("start download url:" + downUrl);
        List<Future<File>> ress = null;
        try {
            //启动计时
            this.startTimer();
            this.downloadTask.setStatus(DownloadTaskStatusEnum.RUNNING.getStatus());
            this.downloadTask.setStatusText("开始解析URL...");
            String m38uUrlContent = HttpClientUtil.getAsString(downUrl);
            logger.info(downUrl + "content:\n" + m38uUrlContent);
            final ArrayList<String> urls = new ArrayList<>();
            final String[] url = m38uUrlContent.split("\n");
            for (String s1 : url) {
                if (!s1.startsWith("#")) {
                    urls.add(s1);
                }
            }
            if (urls.size() == 0) {
                this.downloadTask.setStatusText("没有数据可下载！");
                logger.warning(downUrl + " content is noting!");
                this.downloadTask.setStatus(DownloadTaskStatusEnum.FAILED.getStatus());
                throw new RuntimeException("no data to download!");
            }
            int total = urls.size();
            this.downloadTask.setUrlTotal(total);
            logger.info("download url count:" + total);
            final Integer threadCount = this.downloadTask.getThreadCount();
            logger.info("Use " + threadCount + "threads.");
            final ArrayList<File> downloadingFiles = new ArrayList<>();
            this.downloadTask.setStatusText("开始下载...");
            ress = downloadByMutiThread(urls);
            //等待所有线程下载完成
            for (Future<File> r : ress) {
                downloadingFiles.add(r.get());
            }
            final File parent = new File(systemConfig.getDownloadDir());
            final File file = new File(parent, this.downloadTask.getId() + ".mp4");
            //合并文件
            this.downloadTask.setStatusText("合并文件中...");
            mergeFile(downloadingFiles, file);
            logger.info("m3u8 download video save to " + file.getAbsolutePath());
            this.downloadTask.setStatus(DownloadTaskStatusEnum.FINISHED.getStatus());
            this.downloadTask.setStatusText("完成");
            this.downloadTask.getTaskDomain().setFilePath(file.getAbsolutePath());
            this.downloadTask.setFinishTime(new Date());
            this.downloadTask.finish();
            logger.info("download finished!");
        } catch (InterruptedException ie) {
            logger.info("stop download!");
            this.downloadTask.setStatus(DownloadTaskStatusEnum.STOPPED.getStatus());
            this.downloadTask.setStatusText("已停止");
        } catch (Exception e) {
            this.downloadTask.setStatus(DownloadTaskStatusEnum.FAILED.getStatus());
            this.downloadTask.setStatusText("失败");
            e.printStackTrace();
            logger.severe(e.getMessage());
        } finally {
            if(ress != null){
                for (Future<File> future : ress) {
                    final boolean cancel = future.cancel(true);
                    System.out.println("cancel="+cancel);
                    try {
                        logger.info("cancel "+future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.timer.cancel();
            logger.info("shutdown download thread pool...");
            this.downloadTask.getThreadPool().purge();
            this.downloadTask.getThreadPool().shutdownNow();
            //无论如何最终将状态更新到数据库
            downloadTaskDao.update(this.downloadTask.getTaskDomain());
        }
    }

    private void mergeFile(ArrayList<File> downloadingFiles, File file) throws IOException {
        logger.info("Merging file....");
        final FileOutputStream fos = new FileOutputStream(file);
        for (File downloadingFile : downloadingFiles) {
            DataStreamUtil.copy(downloadingFile, fos, false);
            logger.info(downloadingFile.getName() + "is merged! Delete it...");
            final boolean isDelete = downloadingFile.delete();
            logger.info(isDelete ? "delete success!" : "delete failed!");
        }
        fos.close();
    }

    private List<Future<File>> downloadByMutiThread(ArrayList<String> urls) throws IOException {
        final ArrayList<Future<File>> res = new ArrayList<>();
        final Integer threadCount = this.downloadTask.getThreadCount();
        final int urlCount = urls.size();
        final int per = urlCount / threadCount;
        for (int i = 0; i < threadCount; i++) {
            List<String> sub;
            if (i == threadCount - 1) {
                sub = urls.subList(i * per, urls.size());
            } else {
                sub = urls.subList(i * per, i * per + per);
            }
            final File parent = new File(ApplicationStore.getTmpDir());
            final File file = new File(parent, this.downloadTask.getId() + "_" + i + ".downloading");
            logger.info("download temp file is " + file.getAbsolutePath());
            final Future<File> futureRes = submitDownload(sub, file);
            res.add(futureRes);
        }
        return res;
    }

    private Future<File> submitDownload(List<String> sub, File file) throws IOException {
        final ThreadPoolExecutor threadPool = this.downloadTask.getThreadPool();
        final Integer urlTotal = this.downloadTask.getUrlTotal();
        final String downUrl = this.downloadTask.getUrl();
        FileOutputStream fos = new FileOutputStream(file);
        return threadPool.submit(() -> {
            int n = 0;
            for (String u : sub) {
                if(downloadTask.getStatus() == DownloadTaskStatusEnum.STOPPED.getStatus()) {
                    this.downloadTask.setStatusText("已停止");
                    break;
                }
                if (!u.startsWith("https://") && !u.startsWith("http://")) {
                    u = downUrl.substring(0, downUrl.lastIndexOf("/")) + "/" + u;
                }
                final InputStream is = HttpClientUtil.getAsInputStream(u);
                DataStreamUtil.copy(is, fos, true, false);
                n++;
                logger.info(n + "download：" + u + " success!");
                final double progress = downloadedCount.incrementAndGet() / (double) urlTotal;
                logger.info("download task " + downloadTask.getId() + " current progress is " + progress);
                this.downloadTask.setProgress(progress);
            }
            fos.close();
            return file;
        });
    }

    private void startTimer() {
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                downloadTask.durationIncrement();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }, 0, Thread.NORM_PRIORITY);
    }

    public Timer getTimer(){
        return this.timer;
    }
}
