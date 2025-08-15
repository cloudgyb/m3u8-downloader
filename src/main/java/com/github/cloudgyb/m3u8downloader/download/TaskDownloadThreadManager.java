package com.github.cloudgyb.m3u8downloader.download;

import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务下载线程管理器
 *
 * @author cloudgyb
 * @since 2.0.0
 */
public class TaskDownloadThreadManager {
    private static final Logger logger = LoggerFactory.getLogger(TaskDownloadThreadManager.class);
    private static final TaskDownloadThreadManager instance = new TaskDownloadThreadManager();
    // 下载任务主线程池
    private final ThreadPoolExecutor managerThreadPool;
    // 下载中的任务 Future Map, key 是任务ID，value 是 Future
    private final ConcurrentHashMap<Integer, Future<?>> downloadingTaskFutures = new ConcurrentHashMap<>();

    private TaskDownloadThreadManager() {
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        if (logger.isDebugEnabled()) {
            logger.debug("本机CPU个数为{},创建同等大小的任务下载主线程池", threadPoolSize);
            logger.debug("为了更少的资源占用，最多同时下载CPU个数({})个任务", threadPoolSize);
        }
        this.managerThreadPool = new ThreadPoolExecutor(
                threadPoolSize, threadPoolSize, 0, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(true), new TaskDownloadMainThreadFactory());
    }

    public static TaskDownloadThreadManager getInstance() {
        return instance;
    }

    public void startDownloadThread(DownloadTaskEntity task) {
        int id = task.getId();
        TaskDownloadThread taskDownloadThread = new TaskDownloadThread(task);
        Future<?> future = managerThreadPool.submit(taskDownloadThread);
        downloadingTaskFutures.put(id, future);
    }

    public void stopDownloadThread(DownloadTaskEntity task) {
        Future<?> future = downloadingTaskFutures.get(task.getId());
        if (future == null) {
            return;
        }
        future.cancel(true);
    }

    public void stopAllDownloadThread() {
        for (Future<?> future : downloadingTaskFutures.values()) {
            future.cancel(true);
        }
        downloadingTaskFutures.clear();
    }

    public void shutdown() {
        stopAllDownloadThread();
        if (logger.isDebugEnabled()) {
            logger.debug("优雅关闭线程池...");
        }
        managerThreadPool.shutdown();
        boolean isTerminated = false;
        try {
            isTerminated = managerThreadPool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("等待线程池优雅关闭被中断！");
        }
        if (!isTerminated) {
            if (logger.isDebugEnabled()) {
                logger.debug("开始强制关闭线程池...");
            }
            managerThreadPool.shutdownNow();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("线程池已优雅关闭");
            }
        }
    }

    void removeFuture(int taskId) {
        downloadingTaskFutures.remove(taskId);
    }

    private static class TaskDownloadMainThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(0);

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("TaskDownloadMainThread-" + threadNumber.getAndIncrement());
            return thread;
        }
    }
}
