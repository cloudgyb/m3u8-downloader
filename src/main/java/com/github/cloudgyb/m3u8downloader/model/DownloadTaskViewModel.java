package com.github.cloudgyb.m3u8downloader.model;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.download.TaskDownloadThreadManager;
import com.github.cloudgyb.m3u8downloader.event.*;
import com.github.cloudgyb.m3u8downloader.util.DateFormatter;
import javafx.beans.property.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 下载任务视图模型
 * 所有下载状态的更新都同步到该类，状态的获取来自此类
 *
 * @author cloudgyb
 * 2021/5/16 18:55
 */
@SuppressWarnings("unused")
public class DownloadTaskViewModel implements EventAware {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty saveFilename = new SimpleStringProperty();
    private final StringProperty url = new SimpleStringProperty();
    private final StringProperty createTime = new SimpleStringProperty();
    private final StringProperty rate = new SimpleStringProperty();
    private final ObjectProperty<ProgressAndStatus> progressAndStatus = new SimpleObjectProperty<>();
    private final DownloadTaskEntity taskDomain;
    private final TaskDownloadThreadManager taskDownloadThreadManager = TaskDownloadThreadManager.getInstance();
    private final DownloadTaskService downloadTaskService = DownloadTaskService.getInstance();

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
        String saveFilename = this.taskDomain.getSaveFilename();
        saveFilename = saveFilename == null || saveFilename.isBlank() ?
                taskDomain.getId() + "" : this.taskDomain.getSaveFilename();
        this.saveFilename.set(saveFilename);
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
        this.rate.set("-- KB/s");
    }

    public void start() {
        taskDownloadThreadManager.startDownloadThread(this.taskDomain);
    }


    public void stop() {
        this.rate.set("-- KB/s");
        taskDownloadThreadManager.stopDownloadThread(this.taskDomain);
    }

    public void remove() {
        if (DownloadTaskStageEnum.isRunning(this.progressAndStatus.get().getStage()))
            this.stop();
        ApplicationStore.getNoFinishedTasks().remove(this);
        downloadTaskService.deleteById(taskDomain.getId());
        DownloadTaskStatusChangeEventNotifier eventNotifier = DownloadTaskStatusChangeEventNotifier.INSTANCE;
        eventNotifier.unsubscribe(this);
    }

    public void finish() {
        ApplicationStore.getNoFinishedTasks().remove(this);
        DownloadTaskStatusChangeEventNotifier eventNotifier = DownloadTaskStatusChangeEventNotifier.INSTANCE;
        eventNotifier.unsubscribe(this);
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

    public StringProperty saveFilenameProperty() {
        return saveFilename;
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

    public String getRate() {
        return rate.get();
    }

    public StringProperty rateProperty() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate.set(rate);
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
            DownloadTaskStatusEnum status = progressAndStatus1.getStatus();
            if (DownloadTaskStatusEnum.FINISHED == status) {
                finish();
            }
        } else if (e instanceof DownloadRateChangeEvent) {
            DownloadRateChangeEvent event = (DownloadRateChangeEvent) e;
            int tid = event.getTid();
            // 是否是该任务
            if (tid != this.taskDomain.getId())
                return;
            String rateHumanReadable = event.getRate();
            this.rate.set(rateHumanReadable);
        }
    }

    public void updateSaveFileName(String newSaveFilename) {
        downloadTaskService.updateSaveFilename(this.taskDomain.getId(), newSaveFilename);
    }
}
