package com.github.cloudgyb.m3u8downloader.domain;

import com.github.cloudgyb.m3u8downloader.util.DateFormatter;

import java.io.Serializable;
import java.util.Date;

/**
 * 下载任务domain
 *
 * @author cloudgyb
 * 2021/5/18 11:08
 */
public class DownloadTaskDomain implements Serializable {
    private int id;
    private String url;
    private double progress;
    private volatile int urlTotal;
    private volatile int urlFinished;
    private String urls;
    private int threadCount;
    private volatile int status;
    private volatile long duration;
    private Date createTime;
    private String createTimeText;
    private Date finishTime;
    private String finishTimeText;
    private String filePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public int getUrlTotal() {
        return urlTotal;
    }

    public void setUrlTotal(int urlTotal) {
        this.urlTotal = urlTotal;
    }

    public int getUrlFinished() {
        return urlFinished;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUrlFinished(int urlFinished) {
        this.urlFinished = urlFinished;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getDurationText() {
        long h = this.duration / 3600;
        long m = this.duration / 60;
        long s = this.duration % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public String getCreateTimeText() {
        if (this.createTime != null)
            return DateFormatter.format(this.createTime);
        return "";
    }

    public String getFinishTimeText() {
        if (this.finishTime != null)
            return DateFormatter.format(this.finishTime);
        return "";
    }

    @Override
    public String toString() {
        return "DownloadTaskDomain{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", progress=" + progress +
                ", urlTotal=" + urlTotal +
                ", urlFinished=" + urlFinished +
                ", urls='" + urls + '\'' +
                ", threadCount=" + threadCount +
                ", status=" + status +
                ", duration=" + duration +
                ", createTime=" + createTime +
                ", finishTime=" + finishTime +
                '}';
    }
}
