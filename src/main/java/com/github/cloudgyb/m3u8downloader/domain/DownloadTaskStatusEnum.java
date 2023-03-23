package com.github.cloudgyb.m3u8downloader.domain;

/**
 * 下载任务状态枚举
 * <p>
 * 下载任务状态分为 3 个：新建，运行中，手动停止和异常停止，
 * 该枚举与 {@link DownloadTaskStageEnum} 作用不同，
 * {@link DownloadTaskStageEnum} 用于记录下载处于哪个阶段，而该枚举记录任务的状态
 *
 * @author cloudgyb
 * 2021/5/18 15:46
 */
public enum DownloadTaskStatusEnum {
    NEW("新建"),
    RUNNING("运行中"),
    STOPPED_MANUAL("停止"),
    STOPPED_ERROR("失败"),
    FINISHED("完成");

    final String status;

    DownloadTaskStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
