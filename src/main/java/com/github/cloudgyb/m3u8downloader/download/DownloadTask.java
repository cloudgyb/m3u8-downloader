package com.github.cloudgyb.m3u8downloader.download;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDao;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.domain.SystemConfig;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.util.DataStreamUtil;
import com.github.cloudgyb.m3u8downloader.util.HttpClientUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * 下载任务对象，同时也是下载管理线程，每一个下载任务对应一个管理线程，负责启动下载，其内部维护一个线程池，
 * 真正用于下载数据的正是该线程池
 *
 * @author cloudgyb
 * 2021/5/17 17:09
 */
public class DownloadTask extends Thread {
    private final Logger logger = Logger.getLogger(DownloadTask.class.getSimpleName());

    //下载任务Dao，用于持久化下载任务的状态
    private final DownloadTaskDao downloadTaskDao;
    //下载任务视图模型
    private final DownloadTaskViewModel downloadTaskViewModel;
    //下载完成的URL计数，用于计算下载进度
    private final AtomicInteger downloadedCount = new AtomicInteger(0);
    //系统配置，用于获取下载目录等
    private final SystemConfig systemConfig = ApplicationStore.getSystemConfig();

    //用于下载的线程池
    private volatile ThreadPoolExecutor threadPool;

    public DownloadTask(DownloadTaskViewModel downloadTaskViewModel) {
        this.downloadTaskViewModel = downloadTaskViewModel;
        this.downloadTaskDao = new DownloadTaskDao();
        this.setName("Download-Thread-" + this.downloadTaskViewModel.getId());
    }

    public void run() {
        if (this.downloadTaskViewModel.getStatus() == DownloadTaskStatusEnum.RUNNING.getStatus())
            return;
        //每次开始之前重置计数器
        this.downloadedCount.set(0);
        resetThreadPool();
        final String downUrl = this.downloadTaskViewModel.getUrl();
        logger.info("Start download url: " + downUrl);
        List<Future<File>> ress;
        try {
            this.downloadTaskViewModel.setStatus(DownloadTaskStatusEnum.RUNNING.getStatus());
            this.downloadTaskViewModel.setStatusText("开始解析URL...");
            String m38uUrlContent = HttpClientUtil.getAsString(downUrl);
            final ArrayList<String> urls = new ArrayList<>();
            final String[] url = m38uUrlContent.split("\n");
            for (String s1 : url) {
                if (!s1.startsWith("#")) {
                    urls.add(s1);
                }
            }
            if (urls.size() == 0) {
                this.downloadTaskViewModel.setStatusText("没有数据可下载！");
                logger.warning(downUrl + " content is noting!");
                this.downloadTaskViewModel.setStatus(DownloadTaskStatusEnum.FAILED.getStatus());
                throw new RuntimeException("no data to download!");
            }
            int total = urls.size();
            this.downloadTaskViewModel.setUrlTotal(total);
            logger.info("Download url count: " + total);
            final Integer threadCount = this.downloadTaskViewModel.getThreadCount();
            logger.info("Use " + threadCount + "threads for this download task.");
            final ArrayList<File> downloadingFiles = new ArrayList<>();
            this.downloadTaskViewModel.setStatusText("开始下载...");
            ress = downloadByThreadPool(urls);
            //等待所有线程下载完成
            for (Future<File> r : ress) {
                downloadingFiles.add(r.get());
            }
            //走到这里并不一定所有的任务都下载完了，有可能是手动停止导致了线程池退出了
            if (this.downloadTaskViewModel.getStatus() == DownloadTaskStatusEnum.STOPPING.getStatus()) {
                this.downloadTaskViewModel.setStatus(DownloadTaskStatusEnum.STOPPED.getStatus());
                this.downloadTaskViewModel.setStatusText("已停止");
                return;
            }
            this.downloadTaskViewModel.setStatusText("合并文件中...");
            final File mergedFile = mergeFile(downloadingFiles);
            this.downloadTaskViewModel.setStatus(DownloadTaskStatusEnum.FINISHED.getStatus());
            this.downloadTaskViewModel.setStatusText("完成");
            this.downloadTaskViewModel.getTaskDomain().setFilePath(mergedFile.getAbsolutePath());
            this.downloadTaskViewModel.setFinishTime(new Date());
            this.downloadTaskViewModel.finish();
            logger.info("Download finished!");
        } catch (Exception e) {
            handleDownloadException(e);
        } finally {
            logger.info("Shutting download thread pool...");
            this.threadPool.purge();
            this.threadPool.shutdownNow();
            this.downloadTaskViewModel.stopTimer();
            //无论如何最终将状态更新到数据库
            downloadTaskDao.update(this.downloadTaskViewModel.getTaskDomain());
        }
    }

    /**
     * 处理下载中出现的异常情况
     *
     * @param e 要处理的异常
     */
    private void handleDownloadException(Exception e) {
        logger.severe(e.getMessage());
        //该线程被终止
        if(e instanceof ConnectException) {
            this.downloadTaskViewModel.setStatus(DownloadTaskStatusEnum.FAILED.getStatus());
            this.downloadTaskViewModel.setStatusText("网络未连接");
        } else if (e instanceof InterruptedException) {
            logger.info("Stop download!");
            this.downloadTaskViewModel.setStatus(DownloadTaskStatusEnum.STOPPED.getStatus());
            this.downloadTaskViewModel.setStatusText("已停止");
        //有子任务任务下载超时
        } else if (e instanceof HttpTimeoutException) {
            this.downloadTaskViewModel.setStatus(DownloadTaskStatusEnum.FAILED.getStatus());
            this.downloadTaskViewModel.setStatusText("连接超时");
        //其他异常
        }else{
            this.downloadTaskViewModel.setStatus(DownloadTaskStatusEnum.FAILED.getStatus());
            this.downloadTaskViewModel.setStatusText("失败");
        }
        cleanTempFile();
        e.printStackTrace();
    }


    /**
     * 合并子任务下载的文件到一个文件
     *
     * @param downloadingFiles 子任务下载的文件
     * @throws IOException 如果文件合并失败
     */
    private File mergeFile(ArrayList<File> downloadingFiles) throws IOException {
        final File downTargetDir = new File(systemConfig.getDownloadDir());
        final File targetFile = new File(downTargetDir, this.downloadTaskViewModel.getId() + ".mp4");
        logger.info("Merging file to " + targetFile.getAbsolutePath());
        if (targetFile.exists()) {
            logger.warning("目标文件" + targetFile.getAbsolutePath() + "存在，将被覆盖！");
        }
        final FileOutputStream fos = new FileOutputStream(targetFile);
        for (File downloadingFile : downloadingFiles) {
            DataStreamUtil.copy(downloadingFile, fos, false);
            logger.info(downloadingFile.getName() + "is merged! Delete it...");
            final boolean isDelete = downloadingFile.delete();
            logger.info(isDelete ? "delete success!" : "delete failed!");
        }
        fos.close();
        return targetFile;
    }

    private List<Future<File>> downloadByThreadPool(ArrayList<String> urls) {
        final ArrayList<Future<File>> res = new ArrayList<>();
        final Integer threadCount = this.downloadTaskViewModel.getThreadCount();
        final Integer timeoutRetryCount = systemConfig.getDefaultTimeoutRetryCount();
        final int urlCount = urls.size();
        final int per = urlCount / threadCount;
        for (int i = 0; i < threadCount; i++) {
            List<String> sub;
            if (i == threadCount - 1) {
                sub = urls.subList(i * per, urls.size());
            } else {
                sub = urls.subList(i * per, i * per + per);
            }
            final File parent = new File(ApplicationStore.getTmpDir());
            final File file = new File(parent, this.downloadTaskViewModel.getId() + "_" + i + ".downloading");
            logger.info("download temp file is " + file.getAbsolutePath());
            final SubDownloadTask subDownloadTask =
                    new SubDownloadTask(this.downloadTaskViewModel, sub, file,
                            downloadedCount,timeoutRetryCount);
            final Future<File> futureRes = this.threadPool.submit(subDownloadTask);
            res.add(futureRes);
        }
        return res;
    }

    /**
     * 线程终止之后无法重新使用，所以每次启动下载之前需要重试创建线程池
     */
    private void resetThreadPool() {
        final int threadCount = this.downloadTaskViewModel.getThreadCount();
        //每次开始都新建下载线程池，因为如果此方法调用是下载终止后再次启动下载调用时，
        // 之前的线程池状态已经是“停止”，不能再提交任务了...
        this.threadPool = new ThreadPoolExecutor(0, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                new DownloadThreadFactory(this.downloadTaskViewModel.getId() + ""));
    }

    private void cleanTempFile() {
        logger.info("Cleaning these temp files for downloaded...");
        final int threadCount = this.downloadTaskViewModel.getThreadCount();
        final String tmpDir = ApplicationStore.getTmpDir();
        for (int i = 0; i < threadCount; i++) {
            final File file = new File(tmpDir, this.downloadTaskViewModel.getId() + "_" + i + ".downloading");
            if (file.exists()) {
                final boolean deleted = file.delete();
                if (deleted)
                    logger.info(file.getAbsolutePath() + " is cleaned.");
                else
                    logger.warning(file.getAbsolutePath() + " cannot delete,the file may be occupied.");
            }
        }
    }
}
