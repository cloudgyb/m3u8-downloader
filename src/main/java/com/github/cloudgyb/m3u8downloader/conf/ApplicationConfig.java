package com.github.cloudgyb.m3u8downloader.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置类
 *
 * @author geng
 * @since 2023/03/23 18:40:09
 */
@ConfigurationProperties(prefix = "application.config")
@Component
public class ApplicationConfig {
    private String dbFile;
    private String ffmpegBinFilePath;

    public String getDbFile() {
        return dbFile;
    }

    public void setDbFile(String dbFile) {
        this.dbFile = dbFile;
    }

    public String getFfmpegBinFilePath() {
        return ffmpegBinFilePath;
    }

    public void setFfmpegBinFilePath(String ffmpegBinFilePath) {
        this.ffmpegBinFilePath = ffmpegBinFilePath;
    }
}
