package com.github.cloudgyb.m3u8downloader.proxy;

import com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 启动类
 *
 * @author cloudgyb
 */
public class ProxyApplication {
    private final static String host = "0.0.0.0";
    private final static int port = 80;
    public static final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static volatile ProxyServer proxyServer;

    public static void start() {
        proxyServer = new ProxyServer(host, port, true, "test", "test");
        new Thread(proxyServer::start).start();
    }

    public static void onProxyServerStarted() {
        isRunning.set(true);
    }

    public static void stopProxyServer() {
        if(isRunning.get() && proxyServer != null) {
            proxyServer.stop();
            isRunning.set(false);
        }
    }

}
