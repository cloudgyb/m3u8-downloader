package com.github.cloudgyb.m3u8downloader.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.util.DateFormatter;

import java.util.Date;

/**
 * 下载任务数据库实体类
 *
 * @author geng
 * @since 2023/03/16 21:45:29
 */
@TableName("task")
@SuppressWarnings("unused")
public class DownloadTaskEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String url;
    private Date createTime;
    private Date finishedTime;
    private Integer totalMediaSegment;
    private Integer finishMediaSegment;
    private Long downloadDuration;
    private String filePath;
    private String resolution;
    /**
     * 下载所处的阶段
     *
     * @see DownloadTaskStageEnum
     */
    private String stage;
    /**
     * 任务状态，是停止还是运行中
     */
    private String status;
    private Integer maxThreadCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTotalMediaSegment() {
        return totalMediaSegment;
    }

    public void setTotalMediaSegment(Integer totalMediaSegment) {
        this.totalMediaSegment = totalMediaSegment;
    }

    public Integer getFinishMediaSegment() {
        return finishMediaSegment;
    }

    public void setFinishMediaSegment(Integer finishMediaSegment) {
        this.finishMediaSegment = finishMediaSegment;
    }

    public Long getDownloadDuration() {
        return downloadDuration;
    }

    public void setDownloadDuration(Long downloadDuration) {
        this.downloadDuration = downloadDuration;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Integer getMaxThreadCount() {
        return maxThreadCount;
    }

    public void setMaxThreadCount(Integer maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTimeText() {
        return DateFormatter.format(createTime);
    }

    public String getFinishTimeText() {
        return DateFormatter.format(finishedTime);
    }

    public String getDurationText() {
        return DateFormatter.toDurationText(downloadDuration);
    }
}
