package com.github.cloudgyb.m3u8downloader.download;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务下载业务实现类
 *
 * @author cloudgyb
 * @since 2.0.0
 */
public class TaskDownloadService {
    private static final TaskDownloadService instance = new TaskDownloadService();
    private final static ConcurrentHashMap<Integer, Thread> downloadTaskThread = new ConcurrentHashMap<>();

    public static TaskDownloadService getInstance() {
        return instance;
    }

    public void startDownload(DownloadTaskDomain task) {
        int id = task.getId();
        Thread oldThread = downloadTaskThread.get(id);
        oldThread.interrupt();
        Thread thread = new TaskDownloadThread(task, downloadTaskThread);
        downloadTaskThread.put(id, thread);
        thread.start();
    }

}
