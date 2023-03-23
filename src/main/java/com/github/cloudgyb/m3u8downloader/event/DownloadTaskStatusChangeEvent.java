package com.github.cloudgyb.m3u8downloader.event;

import com.github.cloudgyb.m3u8downloader.model.DownloadTaskStatus;
import com.github.cloudgyb.m3u8downloader.model.ProgressAndStatus;

/**
 * 下载任务状态变更事件
 *
 * @author geng
 * @since 2023/03/16 21:04:50
 */
public class DownloadTaskStatusChangeEvent implements Event {
    private final DownloadTaskStatus downloadTaskStatus;

    public DownloadTaskStatusChangeEvent(DownloadTaskStatus taskStatus) {
        this.downloadTaskStatus = taskStatus;
    }

    public int getTid() {
        return downloadTaskStatus.getTid();
    }

    public ProgressAndStatus getProgressAndStatus() {
        return downloadTaskStatus.getProgressAndStatus();
    }
}
