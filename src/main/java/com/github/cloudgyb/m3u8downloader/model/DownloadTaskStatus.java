package com.github.cloudgyb.m3u8downloader.model;

/**
 * @author geng
 * @since 2023/03/18 16:32:38
 */
public class DownloadTaskStatus {
    private final int tid;
    private final ProgressAndStatus progressAndStatus;

    public DownloadTaskStatus(int tid, ProgressAndStatus progressAndStatus) {
        this.tid = tid;
        this.progressAndStatus = progressAndStatus;
    }

    public int getTid() {
        return tid;
    }

    public ProgressAndStatus getProgressAndStatus() {
        return progressAndStatus;
    }
}
