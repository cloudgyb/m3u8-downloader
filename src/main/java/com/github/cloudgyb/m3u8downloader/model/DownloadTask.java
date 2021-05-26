package com.github.cloudgyb.m3u8downloader.model;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.download.DownloadThread;
import com.github.cloudgyb.m3u8downloader.util.DateFormatter;
import javafx.beans.property.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 下载任务封装类，包装了{@link DownloadTaskDomain},
 * 所有状态的更新都同步到该类，状态的获取来自此类
 *
 * @author cloudgyb
 * 2021/5/16 18:55
 */
public class DownloadTask {
    private final Logger logger = Logger.getLogger(DownloadTask.class.getSimpleName());

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty url = new SimpleStringProperty();
    private final StringProperty createTime = new SimpleStringProperty();
    private final StringProperty duration = new SimpleStringProperty();
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);
    private final StringProperty statusText = new SimpleStringProperty();
    private final IntegerProperty status = new SimpleIntegerProperty();
    private final DownloadTaskDomain taskDomain;
    //线程
    private volatile ThreadPoolExecutor threadPool;
    private volatile DownloadThread downloadThread;


    public DownloadTask(DownloadTaskDomain downloadTaskDomain) {
        this.taskDomain = downloadTaskDomain;
        init();
    }

    private void init() {
        this.setId(this.taskDomain.getId());
        this.setUrl(this.taskDomain.getUrl());
        this.setCreateTime(this.taskDomain.getCreateTimeText());
        this.setStatus(this.taskDomain.getStatus());
        final int status = this.taskDomain.getStatus();
        if (status == DownloadTaskStatusEnum.FAILED.getStatus())
            this.setStatusText("失败");
        else if (status == DownloadTaskStatusEnum.NEW.getStatus())
            this.setStatusText("0%");
        else if (status == DownloadTaskStatusEnum.STOPPED.getStatus())
            this.setStatusText("已停止");
        this.setDuration(this.taskDomain.getDuration());
    }

    public synchronized void start() throws IOException, ExecutionException, InterruptedException {
        if (this.getStatus() == DownloadTaskStatusEnum.RUNNING.getStatus()) {
            return;
        }
        //每次开始都新建线程，因为start()方法如果是线程终止后，再次调用会出现线程状态异常！（线程不能死而复生...）
        this.downloadThread = new DownloadThread(this);
        final int threadCount = this.taskDomain.getThreadCount();
        //每次开始都新建下载线程池，因为如果此方法调用是下载终止后再次启动下载调用时，
        // 之前的线程池状态已经是“停止”，不能再提交任务了...
        this.threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1));
        this.setDuration(0L); //重置计时
        this.downloadThread.start();
    }


    public synchronized void stop() {
        if (downloadThread != null) {
            final Timer timer = this.downloadThread.getTimer();
            if (timer != null)
                timer.cancel();
        }
        if (this.getStatus() == DownloadTaskStatusEnum.RUNNING.getStatus()) {
            if (threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown();
            }
            //直接中断线程
            if (downloadThread != null && !downloadThread.isInterrupted())
                downloadThread.interrupt();
            cleanTempFile();
        }
    }

    public synchronized void remove() {
        if (this.getStatus() == DownloadTaskStatusEnum.RUNNING.getStatus())
            this.stop();
        ApplicationStore.getNoFinishedTasks().remove(this);
    }

    public void finish() {
        ApplicationStore.getNoFinishedTasks().remove(this);
    }

    private void cleanTempFile() {
        logger.info("Cleaning these temp files for downloaded...");
        final int threadCount = this.taskDomain.getThreadCount();
        final String tmpDir = ApplicationStore.getTmpDir();
        for (int i = 0; i < threadCount; i++) {
            final File file = new File(tmpDir, this.getId() + "_" + i + ".downloading");
            if (file.exists()) {
                final boolean deleted = file.delete();
                if (deleted)
                    logger.info(file.getAbsolutePath() + " is cleaned.");
                else
                    logger.warning(file.getAbsolutePath() + " cannot delete,the file may be occupied.");
            }
        }
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public DownloadTaskDomain getTaskDomain() {
        return taskDomain;
    }

    public void setFinishTime(Date finishTime) {
        this.taskDomain.setFinishTime(finishTime);
    }

    public Integer getUrlTotal() {
        return this.taskDomain.getUrlTotal();
    }

    public void setUrlTotal(Integer urlTotal) {
        this.taskDomain.setUrlTotal(urlTotal);
    }

    public Integer getStatus() {
        return this.taskDomain.getStatus();
    }

    public void setStatus(Integer status) {
        this.taskDomain.setStatus(status);
        this.status.set(status);
    }

    public IntegerProperty statusProperty() {
        return this.status;
    }

    public String getStatusText() {
        return statusText.getValue();
    }

    public void setStatusText(String mess) {
        this.statusText.set(mess);
    }

    public StringProperty statusTextProperty() {
        return this.statusText;
    }

    private void setId(int id) {
        this.id.set(id);
    }

    public Integer getId() {
        return this.taskDomain.getId();
    }

    public IntegerProperty idProperty() {
        return this.id;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getUrl() {
        return this.taskDomain.getUrl();
    }

    public StringProperty urlProperty() {
        return this.url;
    }


    public Integer getThreadCount() {
        return this.taskDomain.getThreadCount();
    }

    public String getCreateTime() {
        final Date createTime = this.taskDomain.getCreateTime();
        return DateFormatter.format(createTime);
    }

    public void setCreateTime(String createTime) {
        this.createTime.set(createTime);
    }

    public StringProperty createTimeProperty() {
        return this.createTime;
    }

    public String getDuration() {
        final long duration = this.taskDomain.getDuration();
        return duration + "";
    }

    public void setDuration(Long duration) {
        this.taskDomain.setDuration(duration);
        this.duration.set(this.formatTime(duration));
    }

    public StringProperty durationProperty() {
        return this.duration;
    }


    public void durationIncrement() {
        long duration = this.taskDomain.getDuration() + 1;
        this.setDuration(duration);
    }

    public DoubleProperty progressProperty() {
        return this.progress;
    }

    public void setProgress(Double progress) {
        if (progress == 1.0) {
            this.taskDomain.setStatus(DownloadTaskStatusEnum.FINISHED.getStatus());
            this.status.setValue(DownloadTaskStatusEnum.FINISHED.getStatus());
        }
        String text = String.format("%2.2f", progress * 100) + "%";
        //状态和进度是一列，所以这里也要更新状态文本
        this.setStatusText(text);
        this.taskDomain.setProgress(progress);
        this.progress.set(progress);
    }

    @Override
    public String toString() {
        return this.taskDomain.toString();
    }

    private String formatTime(long time) {
        long h = time / 3600;
        long m = time / 60;
        long s = time % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
