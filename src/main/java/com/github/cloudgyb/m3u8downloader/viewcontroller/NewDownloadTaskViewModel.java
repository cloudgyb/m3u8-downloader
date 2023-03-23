package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.download.TaskDownloadThreadManager;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.util.SpringBeanUtil;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

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
    private final DownloadTaskService downloadTaskService = SpringBeanUtil.getBean(DownloadTaskService.class);

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

    public void setTaskMaxThreadCount(double taskMaxThreadCount) {
        this.taskMaxThreadCount.set(taskMaxThreadCount);
    }


    public void download() {
        DownloadTaskEntity entity = convertToDomain();
        downloadTaskService.save(entity);
        final DownloadTaskViewModel task = new DownloadTaskViewModel(entity);
        ApplicationStore.getNoFinishedTasks().add(task);
        // TaskDownloadService 负责整个下载过程
        TaskDownloadThreadManager.getInstance().startDownloadThread(entity);
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
        domain.setStage(DownloadTaskStageEnum.NEW.name());
        domain.setStatus(DownloadTaskStageEnum.NEW.name());
        return domain;
    }
}
