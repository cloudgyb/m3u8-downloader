package com.github.cloudgyb.m3u8downloader.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author geng
 * @since 2023/03/16 21:59:08
 */
@TableName("media_segment")
@SuppressWarnings("unused")
public class MediaSegmentEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    private Integer taskId;
    private String url;
    private Boolean finished;
    private Long duration;
    private Long downloadDuration;
    private String filePath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
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

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
