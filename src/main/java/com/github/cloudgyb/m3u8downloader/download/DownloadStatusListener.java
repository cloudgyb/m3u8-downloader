package com.github.cloudgyb.m3u8downloader.download;

/**
 * 下载状态监听器，对下载状态改变感兴趣的类都应实现此类，
 * 例如视图模型类需要根据下载状态来更新视图
 *
 * @author cloudgyb
 * 2021/6/7 16:50
 */
public interface DownloadStatusListener {
    /**
     * 下载任务正式启动时
     **/
    void onStart();

    /**
     * 下载任务停止时（手动停止）
     **/
    void onStop();

    /**
     * 下载任务异常终止
     */
    void onFailed(Exception e);

    /**
     * 当下载完成时
     */
    void onFinished();

    /**
     * 当进度改变时
     *
     * @param progress 新的进度值，1.0代表100%
     */
    void onProgressChange(double progress);

}
