package com.github.cloudgyb.m3u8downloader.conf;

/**
 * 网络代理配置
 *
 * @author cloudgyb
 * @since 2025/8/17 15:45
 */
public final class ProxyConfig {
    private final String proxyHost;
    private final int proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final boolean proxyEnabled;

    public ProxyConfig(String proxyHost, int proxyPort,
                       String proxyUsername, String proxyPassword,
                       boolean proxyEnabled) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.proxyEnabled = proxyEnabled;
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
}
