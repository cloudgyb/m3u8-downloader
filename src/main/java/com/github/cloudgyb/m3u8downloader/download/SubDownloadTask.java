package com.github.cloudgyb.m3u8downloader.download;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.util.DataStreamUtil;
import com.github.cloudgyb.m3u8downloader.util.HttpClientUtil;

import java.io.*;
import java.net.http.HttpTimeoutException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * 下载子任务对象，每一个下载任务可以分割为多个子下载任务，该子下载任务用于提交到线程池进行执行
 * 该类实现了Callable接口，用于异步获取结果，结果就是下载完成的临时文件
 *
 * @author cloudgyb
 * 2021/6/7 16:26
 */
public class SubDownloadTask implements Callable<File> {
    private final Logger logger = Logger.getLogger(SubDownloadTask.class.getSimpleName());
    /**
     * 下载任务
     */
    private final DownloadTaskViewModel downloadTaskViewModel;
    /**
     * 分配给该子任务的下载连接
     **/
    private final List<String> urls;
    /**
     * 用于存储下载数据
     */
    private final File tmpFile;
    private final AtomicInteger downloadedCount;
    /**
     * 下载超时重试次数
     */
    private final int timeoutRetryCount;

    public SubDownloadTask(DownloadTaskViewModel downloadTaskViewModel, List<String> urls,
                           File tmpFile,
                           AtomicInteger downloadedCount,
                           int timeoutRetryCount) {
        if(timeoutRetryCount <= 0)
            throw new IllegalArgumentException("timeoutRetryCount not allow <= 0!");
        this.downloadTaskViewModel = downloadTaskViewModel;
        this.urls = urls;
        this.tmpFile = tmpFile;
        this.downloadedCount = downloadedCount;
        this.timeoutRetryCount = timeoutRetryCount;
    }

    @Override
    public File call() throws Exception {
        final Integer urlTotal = this.downloadTaskViewModel.getUrlTotal();
        final String downUrl = this.downloadTaskViewModel.getUrl();
        FileOutputStream fos = new FileOutputStream(tmpFile);
        int n = 0;
        for (String u : this.urls) {
            //随时有可能被停止，这里没下载完成一个URL就进行判断是否停止
            if (downloadTaskViewModel.getStatus() != DownloadTaskStatusEnum.RUNNING.getStatus()) {
                logger.info("Download task is stopping,the Sub-Task will exit!");
                break;
            }
            if (!u.startsWith("https://") && !u.startsWith("http://")) {
                u = downUrl.substring(0, downUrl.lastIndexOf("/")) + "/" + u;
            }
            boolean isDownOk = false;
            int timeoutCount = 0;
            byte[] data = null;
            while(!isDownOk) {
                try {
                    data = HttpClientUtil.getAsByte(u);
                    isDownOk = true;
                }catch (HttpTimeoutException ignore){
                    timeoutCount ++;
                    if(timeoutCount > this.timeoutRetryCount) {
                        throw new HttpTimeoutException("下载超时！");
                    }
                    logger.warning("Download "+ u +"+ timeout,start retry "+timeoutCount);
                }
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            DataStreamUtil.copy(bis, fos, true, false);
            n++;
            logger.info(n + "download：" + u + " success!");
            final double progress = downloadedCount.incrementAndGet() / (double) urlTotal;
            logger.info("download task " + downloadTaskViewModel.getId() + " current progress is " + progress);
            this.downloadTaskViewModel.setProgress(progress);
        }
        fos.close();
        return tmpFile;
    }
}
