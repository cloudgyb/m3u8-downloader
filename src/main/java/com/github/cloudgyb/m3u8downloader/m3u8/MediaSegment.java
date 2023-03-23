package com.github.cloudgyb.m3u8downloader.m3u8;

/**
 * @author geng
 * @since 2023/03/16 22:04:06
 */
public class MediaSegment {
    private final String url;
    private final double duration;

    public MediaSegment(String url, double duration) {
        this.url = url;
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public double getDuration() {
        return duration;
    }
}
