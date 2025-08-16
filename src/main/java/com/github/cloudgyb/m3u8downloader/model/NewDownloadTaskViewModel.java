package com.github.cloudgyb.m3u8downloader.model;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.download.TaskDownloadThreadManager;
import com.github.cloudgyb.m3u8downloader.viewcontroller.Alerts;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;
import java.util.concurrent.RejectedExecutionException;

/**
 * 任务新建视图模型
 *
 * @author cloudgyb
 * @since 2023/03/15 22:46:48
 */
public class NewDownloadTaskViewModel {
    /**
     * 该任务对应的下载 url
     */
    private final StringProperty url = new SimpleStringProperty();
    /**
     * 该任务允许使用的最大线程数
     */
    private final DoubleProperty taskMaxThreadCount = new SimpleDoubleProperty();
    /**
     * 保存的文件名
     */
    private final StringProperty filename = new SimpleStringProperty();
    private final DownloadTaskService downloadTaskService = DownloadTaskService.getInstance();

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public double getTaskMaxThreadCount() {
        return taskMaxThreadCount.get();
    }

    public DoubleProperty taskMaxThreadCountProperty() {
        return taskMaxThreadCount;
    }

    @SuppressWarnings("unused")
    public void setTaskMaxThreadCount(double taskMaxThreadCount) {
        this.taskMaxThreadCount.set(taskMaxThreadCount);
    }

    public StringProperty filenameProperty() {
        return filename;
    }

    public String getFilename() {
        return filename.get();
    }

    public void download() {
        DownloadTaskEntity entity = convertToDomain();
        downloadTaskService.save(entity);
        final DownloadTaskViewModel task = new DownloadTaskViewModel(entity);
        ApplicationStore.getNoFinishedTasks().add(task);
        // TaskDownloadThreadManager 负责整个下载过程
        try {
            TaskDownloadThreadManager.getInstance().startDownloadThread(entity);
        } catch (RejectedExecutionException ignore) {
            Alerts.alert("开始失败", "提示", "任务已达最大并发数，请稍后重试！");
        }
    }

    public DownloadTaskEntity convertToDomain() {
        final DownloadTaskEntity domain = new DownloadTaskEntity();
        domain.setUrl(getUrl());
        domain.setCreateTime(new Date());
        domain.setDownloadDuration(0L);
        domain.setFinishMediaSegment(0);
        domain.setTotalMediaSegment(0);
        int maxThread = Double.valueOf(getTaskMaxThreadCount()).intValue();
        if (maxThread == 0) {
            maxThread = ApplicationStore.getSystemConfig().getDefaultThreadCount();
        }
        domain.setMaxThreadCount(maxThread);
        domain.setSaveFilename(getFilename());
        domain.setStage(DownloadTaskStageEnum.NEW.name());
        domain.setStatus(DownloadTaskStatusEnum.NEW.name());
        return domain;
    }
}
