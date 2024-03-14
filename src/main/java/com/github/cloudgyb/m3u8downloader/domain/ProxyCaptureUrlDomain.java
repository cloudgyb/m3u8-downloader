package com.github.cloudgyb.m3u8downloader.domain;

/**
 * 代理服务器自动捕获的 url
 *
 * @author cloudgyb
 */
public class ProxyCaptureUrlDomain {
    private String host;
    private String url;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
