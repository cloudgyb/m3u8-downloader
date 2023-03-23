package com.github.cloudgyb.m3u8downloader.event;

/**
 * 下载速率改变事件
 *
 * @author geng
 * @since 2023/03/22 17:39:00
 */
public class DownloadRateChangeEvent implements Event {
    private final Integer tid;
    private final String rate;

    public DownloadRateChangeEvent(Integer tid, String rate) {
        this.tid = tid;
        this.rate = rate;
    }

    public Integer getTid() {
        return tid;
    }

    public String getRate() {
        return rate;
    }
}
