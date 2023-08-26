package com.github.cloudgyb.m3u8downloader.proxy.server;

public final class ProxyServerAuthenticationConfig {

    private final boolean enableProxyAuth;
    private final String username;
    private final String password;

    public ProxyServerAuthenticationConfig(boolean enableProxyAuth, String username, String password) {
        this.enableProxyAuth = enableProxyAuth;
        this.username = username;
        this.password = password;
    }

    public boolean enableProxyAuth() {
        return enableProxyAuth;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
