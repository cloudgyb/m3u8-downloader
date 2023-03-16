package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import com.github.cloudgyb.m3u8downloader.download.TaskDownloadService;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
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

    /**
     * 新任务下载业务逻辑实现类
     */
    private final NewDownloadTaskModel newDownloadTaskModel = new NewDownloadTaskModel();

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
        DownloadTaskDomain downloadTaskDomain = convertToDomain();
        newDownloadTaskModel.save(downloadTaskDomain);
        final DownloadTaskViewModel task = new DownloadTaskViewModel(downloadTaskDomain);
        ApplicationStore.getNoFinishedTasks().add(task);
        // TaskDownloadService 负责整个下载过程
        TaskDownloadService.getInstance().startDownload(downloadTaskDomain);
    }

    public DownloadTaskDomain convertToDomain() {
        final DownloadTaskDomain domain = new DownloadTaskDomain();
        domain.setUrl(getUrl());
        domain.setUrlFinished(0);
        domain.setStatus(0);
        domain.setProgress(0);
        domain.setDuration(0);
        domain.setCreateTime(new Date());
        domain.setThreadCount(Double.valueOf(getTaskMaxThreadCount()).intValue());
        return domain;
    }
}
