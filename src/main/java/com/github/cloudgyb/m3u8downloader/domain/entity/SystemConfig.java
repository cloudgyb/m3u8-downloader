package com.github.cloudgyb.m3u8downloader.domain.entity;

import com.github.cloudgyb.m3u8downloader.conf.ProxyConfig;

import java.io.File;
import java.io.Serializable;

/**
 * 系统配置
 *
 * @author cloudgyb
 * 2021/5/19 9:35
 */
public class SystemConfig implements Serializable {
    private Integer id = 1;
    private String downloadDir = System.getProperty("user.home") +
            File.separator + "Downloads" + File.separator;;
    private Integer defaultThreadCount = 5;
    private Integer defaultTimeoutRetryCount;
    private ProxyConfig proxyConfig = new ProxyConfig();

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

    public Integer getDefaultTimeoutRetryCount() {
        return defaultTimeoutRetryCount;
    }

    public void setDefaultTimeoutRetryCount(Integer defaultTimeoutRetryCount) {
        this.defaultTimeoutRetryCount = defaultTimeoutRetryCount;
    }

    public void setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }
}
