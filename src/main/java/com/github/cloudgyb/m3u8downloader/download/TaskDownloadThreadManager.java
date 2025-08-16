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
    // 下载任务主线程池，负责整个下载过程
    private final ThreadPoolExecutor managerThreadPool;
    // 下载任务工作线程池，负责下载任务片段
    private final ThreadPoolExecutor workerThreadPool;
    // 下载中的任务 Future Map, key 是任务ID，value 是 Future
    private final ConcurrentHashMap<Integer, Future<?>> downloadingTaskFutures = new ConcurrentHashMap<>();

    private TaskDownloadThreadManager() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        if (logger.isDebugEnabled()) {
            logger.debug("本机CPU个数为{},创建同等大小的任务下载主线程池", cpuCores);
            logger.debug("为了更少的资源占用，最多同时下载CPU个数({})个任务", cpuCores);
        }
        this.managerThreadPool = new ThreadPoolExecutor(
                cpuCores, cpuCores, 0, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(true),
                new TaskDownloadThreadFactory("TaskDownloadManagerThread-"));
        this.workerThreadPool = new ThreadPoolExecutor(
                cpuCores * 10, cpuCores * 20,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new TaskDownloadThreadFactory("TaskDownloadWorkerThread-"),
                new ThreadPoolExecutor.AbortPolicy());
        // 允许核心线程超时，当无下载任务时避免资源浪费
        this.workerThreadPool.allowCoreThreadTimeOut(true);
    }

    public static TaskDownloadThreadManager getInstance() {
        return instance;
    }

    public void startDownloadThread(DownloadTaskEntity task) {
        int id = task.getId();
        TaskDownloadThread taskDownloadThread = new TaskDownloadThread(task, workerThreadPool);
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
        workerThreadPool.shutdown();
        managerThreadPool.shutdown();
        boolean isTerminated = false;
        boolean isWorkerThreadPoolTerminated = false;
        try {
            isWorkerThreadPoolTerminated = workerThreadPool.awaitTermination(2, TimeUnit.SECONDS);
            isTerminated = managerThreadPool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("等待线程池优雅关闭被中断！");
        }
        if(!isWorkerThreadPoolTerminated) {
            workerThreadPool.shutdownNow();
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

    private static class TaskDownloadThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(0);
        private final String threadNamePrefix;

        public TaskDownloadThreadFactory(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(threadNamePrefix + threadNumber.getAndIncrement());
            return thread;
        }
    }
}
