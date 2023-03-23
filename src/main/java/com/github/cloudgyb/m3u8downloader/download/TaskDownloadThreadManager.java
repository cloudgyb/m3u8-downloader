package com.github.cloudgyb.m3u8downloader.download;

import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务下载线程管理器
 *
 * @author cloudgyb
 * @since 2.0.0
 */
public class TaskDownloadThreadManager {
    private static final TaskDownloadThreadManager instance = new TaskDownloadThreadManager();
    private final static ConcurrentHashMap<Integer, Thread> downloadTaskThread = new ConcurrentHashMap<>();

    public static TaskDownloadThreadManager getInstance() {
        return instance;
    }

    public void startDownloadThread(DownloadTaskEntity task) {
        int id = task.getId();
        Thread oldThread = downloadTaskThread.get(id);
        if (oldThread != null) {
            // 如果老的线程已经终止，创建一个新的
            if (oldThread.getState() == Thread.State.TERMINATED) {
                createDownloadThread(task);
                startDownloadThread(task);
            } else if (oldThread.getState() == Thread.State.NEW) {
                oldThread.start();
            }
        } else {
            createDownloadThread(task);
            startDownloadThread(task);
        }
    }

    public void stopDownloadThread(DownloadTaskEntity task) {
        Thread thread = downloadTaskThread.get(task.getId());
        if (thread == null) {
            return;
        }
        if (thread instanceof TaskDownloadThread) {
            TaskDownloadThread downloadThread = (TaskDownloadThread) thread;
            downloadThread.stopDownload();
            downloadTaskThread.remove(task.getId());
        }
    }

    public void createDownloadThread(DownloadTaskEntity task) {
        downloadTaskThread.put(task.getId(), new TaskDownloadThread(task));
    }
}
