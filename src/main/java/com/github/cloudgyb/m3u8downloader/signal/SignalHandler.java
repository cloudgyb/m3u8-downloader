package com.github.cloudgyb.m3u8downloader.signal;

public interface SignalHandler {
    boolean canHandle(String signal);

    void handle(String signal);
}
