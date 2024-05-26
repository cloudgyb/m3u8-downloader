package com.github.cloudgyb.m3u8downloader.signal;

import com.github.cloudgyb.m3u8downloader.M3u8DownloaderApp;

public class RepeatProcessStartupSignalHandler implements SignalHandler {
    public static final String signal = "repeatProcessStartupSignal\r\n";

    @Override
    public boolean canHandle(String sig) {
        return signal.equals(sig);
    }

    @Override
    public void handle(String signal) {
        M3u8DownloaderApp.toFront();
    }
}
