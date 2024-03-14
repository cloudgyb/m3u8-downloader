package com.github.cloudgyb.m3u8downloader.proxy;

import com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 启动类
 *
 * @author cloudgyb
 */
public class ProxyApplication {
    public final static String host = "0.0.0.0";
    public final static int port = 80;
    public static final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static volatile ProxyServer proxyServer;

    @FunctionalInterface
    public interface StartedCallback {
        void started(boolean isStarted);
    }

    public interface StopCallback {
        void stopped(boolean isStopped);
    }

    public static void start(StartedCallback startedCallback) {
        proxyServer = new ProxyServer(host, port, true, "test", "test");
        new Thread(() -> proxyServer.start(startedCallback)).start();
    }

    public static void onProxyServerStarted() {
        isRunning.set(true);
    }

    public static void stopProxyServer(StopCallback stopCallback) {
        if (isRunning.get() && proxyServer != null) {
            proxyServer.stop(stopCallback);
            isRunning.set(false);
        } else {
            stopCallback.stopped(true);
        }
    }

    public static void onProxyServerStartFailed() {
    }
}
