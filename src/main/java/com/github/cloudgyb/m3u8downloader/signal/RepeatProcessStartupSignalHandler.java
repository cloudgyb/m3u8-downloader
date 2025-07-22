package com.github.cloudgyb.m3u8downloader.signal;

import com.github.cloudgyb.m3u8downloader.M3u8DownloaderApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepeatProcessStartupSignalHandler implements SignalHandler {
    public static final String signal = "repeatProcessStartupSignal\r\n";
    private static final Logger log = LoggerFactory.getLogger(RepeatProcessStartupSignalHandler.class);

    @Override
    public boolean canHandle(String sig) {
        return signal.equals(sig);
    }

    @Override
    public void handle(String signal) {
        log.info("接收到重复启动信号: {}", signal);
        M3u8DownloaderApp.toFront();
    }
}
