package com.github.cloudgyb.m3u8downloader.domain;

/**
 * 下载任务状态枚举
 *
 * @author cloudgyb
 * 2021/5/18 15:46
 */
public enum DownloadTaskStatusEnum {
    //当前任务下载状态，0未开始，1进行中，2已停止,3已完成,-1失败
    NEW(0), RUNNING(1), STOPPED(2), FINISHED(3), FAILED(-1);

    int status;

    DownloadTaskStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
