package com.github.cloudgyb.m3u8downloader.conf;

import java.util.Properties;

/**
 * 应用配置类
 * 自动加载 application.properties 属性文件
 *
 * @author geng
 * @since 2023/03/23 18:40:09
 */
public class ApplicationConfig {
    private static volatile ApplicationConfig instance;
    private final String ffmpegBinFilePath;

    public ApplicationConfig(String ffmpegBinFilePath) {
        this.ffmpegBinFilePath = ffmpegBinFilePath;
    }

    public static ApplicationConfig getInstance() {
        if (instance == null) {
            synchronized (ApplicationConfig.class) {
                if (instance == null) {
                    Properties properties = new Properties();
                    try {
                        properties.load(ApplicationConfig.class.getClassLoader()
                                .getResourceAsStream("application.properties"));
                    } catch (Exception e) {
                        throw new RuntimeException("未找到 application.properties 文件！");
                    }
                    String ffmpegBinFilePath = properties.getProperty("application.config.ffmpeg-bin-file-path");
                    instance = new ApplicationConfig(ffmpegBinFilePath);
                }
            }
        }
        return instance;
    }

    public String getFfmpegBinFilePath() {
        return ffmpegBinFilePath;
    }
}
