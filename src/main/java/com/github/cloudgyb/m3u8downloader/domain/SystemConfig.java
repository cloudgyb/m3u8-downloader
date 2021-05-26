package com.github.cloudgyb.m3u8downloader.domain;

import java.io.Serializable;

/**
 * 系统配置
 *
 * @author cloudgyb
 * 2021/5/19 9:35
 */
public class SystemConfig implements Serializable {
    private Integer id;
    private String downloadDir;
    private Integer defaultThreadCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    public Integer getDefaultThreadCount() {
        return defaultThreadCount;
    }

    public void setDefaultThreadCount(Integer defaultThreadCount) {
        this.defaultThreadCount = defaultThreadCount;
    }
}
