package com.github.cloudgyb.m3u8downloader.download;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import com.github.cloudgyb.m3u8downloader.m3u8.M3U8Parser;

import java.util.concurrent.ConcurrentHashMap;

public class TaskDownloadThread extends Thread {
    //private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor();
    private final M3U8Parser m3U8Parser = new M3U8Parser();
    private final DownloadTaskDomain task;
    private final ConcurrentHashMap<Integer, Thread> downloadTaskThread;

    public TaskDownloadThread(DownloadTaskDomain task, ConcurrentHashMap<Integer, Thread> downloadTaskThread) {
        this.task = task;
        this.downloadTaskThread = downloadTaskThread;
    }

    @Override
    public void run() {
        int id = task.getId();
    }
}
