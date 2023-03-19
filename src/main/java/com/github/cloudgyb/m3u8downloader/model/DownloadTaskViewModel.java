package com.github.cloudgyb.m3u8downloader.model;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.download.TaskDownloadThreadManager;
import com.github.cloudgyb.m3u8downloader.event.DownloadTaskStatusChangeEvent;
import com.github.cloudgyb.m3u8downloader.event.DownloadTaskStatusChangeEventNotifier;
import com.github.cloudgyb.m3u8downloader.event.Event;
import com.github.cloudgyb.m3u8downloader.event.EventAware;
import com.github.cloudgyb.m3u8downloader.util.DateFormatter;
import com.github.cloudgyb.m3u8downloader.util.SpringBeanUtil;
import javafx.beans.property.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 下载任务视图模型，包装了{@link DownloadTaskDomain},
 * 所有下载状态的更新都同步到该类，状态的获取来自此类
 *
 * @author cloudgyb
 * 2021/5/16 18:55
 */
@SuppressWarnings("unused")
public class DownloadTaskViewModel implements EventAware {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty url = new SimpleStringProperty();
    private final StringProperty createTime = new SimpleStringProperty();
    private final StringProperty duration = new SimpleStringProperty();
    private final ObjectProperty<ProgressAndStatus> progressAndStatus = new SimpleObjectProperty<>();
    private final DownloadTaskEntity taskDomain;
    private final TaskDownloadThreadManager taskDownloadThreadManager = TaskDownloadThreadManager.getInstance();
    //计时器，用于下载时间计时
    private Timer timer;
    private final DownloadTaskService downloadTaskService = SpringBeanUtil.getBean(DownloadTaskService.class);

    /**
     * 构造一个下载任务视图模型
     **/
    public DownloadTaskViewModel(DownloadTaskEntity entity) {
        this.taskDomain = entity;
        this.init();
        // 订阅任务状态变更时间
        DownloadTaskStatusChangeEventNotifier eventNotifier = DownloadTaskStatusChangeEventNotifier.INSTANCE;
        eventNotifier.subscribe(this);
    }

    //视图模型初始化
    private void init() {
        this.id.set(this.taskDomain.getId());
        this.url.set(this.taskDomain.getUrl());
        this.createTime.set(DateFormatter.format(this.taskDomain.getCreateTime()));
        String statusText = this.taskDomain.getStage();
        String status = this.taskDomain.getStatus();
        DownloadTaskStatusEnum statusEnum = DownloadTaskStatusEnum.valueOf(status);
        if (statusEnum == DownloadTaskStatusEnum.RUNNING) {
            statusEnum = DownloadTaskStatusEnum.STOPPED_ERROR;
        }
        DownloadTaskStageEnum stage = DownloadTaskStageEnum.valueOf(statusText);
        Integer finishMediaSegment = this.taskDomain.getFinishMediaSegment();
        Integer totalMediaSegment = this.taskDomain.getTotalMediaSegment();
        double progressValue = totalMediaSegment == 0 ? 0D : (double) finishMediaSegment / totalMediaSegment;
        this.progressAndStatus.set(new ProgressAndStatus(statusEnum, progressValue, stage));
        this.setDuration(this.taskDomain.getDownloadDuration());
    }

    public void start() {
        taskDownloadThreadManager.startDownloadThread(this.taskDomain);
        this.startTimer();
    }


    public void stop() {
        taskDownloadThreadManager.stopDownloadThread(this.taskDomain);
        stopTimer();
    }

    public void remove() {
        if (DownloadTaskStageEnum.isRunning(this.progressAndStatus.get().getStage()))
            this.stop();
        ApplicationStore.getNoFinishedTasks().remove(this);
        downloadTaskService.deleteById(taskDomain.getId());
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

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getUrl() {
        return this.taskDomain.getUrl();
    }

    public DownloadTaskStatusEnum getStatus() {
        return this.progressAndStatus.get().getStatus();
    }

    public void setDuration(Long duration) {
        this.duration.set(DateFormatter.toDurationText(duration));
    }

    public void durationIncrement() {
        long duration = this.taskDomain.getDownloadDuration() + 1;
        this.setDuration(duration);
    }

    @Override
    public String toString() {
        return this.taskDomain.toString();
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty urlProperty() {
        return url;
    }

    public String getCreateTime() {
        return createTime.get();
    }

    public StringProperty createTimeProperty() {
        return createTime;
    }

    public String getDuration() {
        return duration.get();
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public ProgressAndStatus getProgressAndStatus() {
        return progressAndStatus.get();
    }

    public ObjectProperty<ProgressAndStatus> progressAndStatusProperty() {
        return progressAndStatus;
    }

    @Override
    public void notify(Event e) {
        if (e instanceof DownloadTaskStatusChangeEvent) {
            DownloadTaskStatusChangeEvent event = (DownloadTaskStatusChangeEvent) e;
            int tid = event.getTid();
            // 是否是该任务
            if (tid != this.taskDomain.getId())
                return;
            ProgressAndStatus progressAndStatus1 = event.getProgressAndStatus();
            logger.info("接收到任务状态变更通知：{}", progressAndStatus1.toString());
            this.progressAndStatus.setValue(progressAndStatus1);
        }
    }
}
