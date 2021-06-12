package com.github.cloudgyb.m3u8downloader.model;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.download.DownloadTask;
import com.github.cloudgyb.m3u8downloader.util.DateFormatter;
import javafx.beans.property.*;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * 下载任务视图模型，包装了{@link DownloadTaskDomain},
 * 所有下载状态的更新都同步到该类，状态的获取来自此类
 *
 * @author cloudgyb
 * 2021/5/16 18:55
 */
public class DownloadTaskViewModel {
    private final Logger logger = Logger.getLogger(DownloadTaskViewModel.class.getSimpleName());

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty url = new SimpleStringProperty();
    private final StringProperty createTime = new SimpleStringProperty();
    private final StringProperty duration = new SimpleStringProperty();
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);
    private final StringProperty statusText = new SimpleStringProperty();
    private final IntegerProperty status = new SimpleIntegerProperty();

    private final DownloadTaskDomain taskDomain;
    private volatile DownloadTask downloadTask;

    //计时器，用于下载时间计时
    private Timer timer;


    /**
     * 构造一个下载任务视图模型
     *
     * @param downloadTaskDomain 该参数包含了下载所需的所有参数配置
     */
    public DownloadTaskViewModel(DownloadTaskDomain downloadTaskDomain) {
        this.taskDomain = downloadTaskDomain;

        this.init();
    }

    //视图模型初始化
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
        this.setProgress(0.0);
        this.setDuration(0L); //重置计时
        //重新创建下载线程
        this.downloadTask = new DownloadTask(this);
        this.downloadTask.start();
        this.startTimer();
    }


    public synchronized void stop() {
        //只是设置一个状态，当下载线程获取到该状态后会自动停止
        this.setStatus(DownloadTaskStatusEnum.STOPPING.getStatus());
        this.setStatusText("正在停止...");
        if(this.downloadTask != null)
            this.downloadTask.interrupt();
        stopTimer();
    }

    public synchronized void remove() {
        if (this.getStatus() == DownloadTaskStatusEnum.RUNNING.getStatus())
            this.stop();
        ApplicationStore.getNoFinishedTasks().remove(this);
        if(this.downloadTask != null)
            this.downloadTask.interrupt();
        stopTimer();
    }

    public void finish() {
        ApplicationStore.getNoFinishedTasks().remove(this);
        stopTimer();
    }

    private synchronized void startTimer() {
        if (this.timer != null)
            this.timer.cancel();
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                durationIncrement();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }, 0, Thread.NORM_PRIORITY);
    }

    public synchronized void stopTimer() {
        if (this.timer != null)
            this.timer.cancel();
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
