package com.github.cloudgyb.m3u8downloader.model;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;

/**
 * 进度值和状态描述 pojo
 *
 * @author geng
 * @since 2023/03/18 15:18:05
 */
public class ProgressAndStatus {
    private final Double progress;
    private final DownloadTaskStageEnum stage;
    private final DownloadTaskStatusEnum status;

    public ProgressAndStatus(DownloadTaskStatusEnum status, Double progress, DownloadTaskStageEnum stage) {
        this.progress = progress;
        this.stage = stage;
        this.status = status;
    }

    public Double getProgress() {
        return progress;
    }

    public DownloadTaskStageEnum getStage() {
        return stage;
    }

    public DownloadTaskStatusEnum getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "progress=" + progress +
                ", status=" + stage;
    }
}
