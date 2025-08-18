package com.github.cloudgyb.m3u8downloader.conf;

import java.net.Proxy;

/**
 * 网络代理配置
 *
 * @author cloudgyb
 * @since 2025/8/17 15:45
 */
public final class ProxyConfig {
    private volatile String proxyHost;
    private volatile int proxyPort;
    private volatile String proxyUsername;
    private volatile String proxyPassword;
    private volatile boolean proxyEnabled;
    private volatile Proxy.Type proxyType;

    public ProxyConfig() {
        this.proxyHost = "";
        this.proxyPort = 0;
        this.proxyUsername = "";
        this.proxyPassword = "";
        this.proxyEnabled = false;
        this.proxyType = Proxy.Type.SOCKS;
    }

    public ProxyConfig(String proxyHost, int proxyPort,
                       String proxyUsername, String proxyPassword,
                       boolean proxyEnabled, Proxy.Type proxyType) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.proxyEnabled = proxyEnabled;
        this.proxyType = proxyType;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    public Proxy.Type getProxyType() {
        return proxyType;
    }

    public void setProxyType(Proxy.Type proxyType) {
        this.proxyType = proxyType;
    }

    public void update(ProxyConfig proxyConfig) {
        this.proxyHost = proxyConfig.getProxyHost();
        this.proxyPort = proxyConfig.getProxyPort();
        this.proxyUsername = proxyConfig.getProxyUsername();
        this.proxyPassword = proxyConfig.getProxyPassword();
        this.proxyEnabled = proxyConfig.isProxyEnabled();
        this.proxyType = proxyConfig.getProxyType();
    }
    @Override
    public String toString() {
        return "proxyHost=" + proxyHost +
                ", proxyPort=" + proxyPort +
                ", proxyUsername=" + proxyUsername +
                ", proxyPassword=******" +
                ", proxyEnabled=" + proxyEnabled +
                ", proxyType=" + proxyType;
    }
}
