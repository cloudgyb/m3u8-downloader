package com.github.cloudgyb.m3u8downloader.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.List;

/**
 * SOCKS (v4 or v5)代理选择器
 *
 * @author cloudgyb
 * @since 2025/08/18 16:49
 */
public class SocksProxySelector extends ProxySelector {
    private static final Logger log = LoggerFactory.getLogger(SocksProxySelector.class);
    private final Proxy proxy;

    public SocksProxySelector(String proxyHost, int proxyPort) {
        this.proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, proxyPort));
    }

    @Override
    public List<Proxy> select(URI uri) {
        return List.of(proxy);
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        log.error("代理连接失败！ {}", ioe.getMessage());
    }
}
