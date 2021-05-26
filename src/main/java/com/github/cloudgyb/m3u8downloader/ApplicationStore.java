package com.github.cloudgyb.m3u8downloader;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDao;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import com.github.cloudgyb.m3u8downloader.domain.SystemConfig;
import com.github.cloudgyb.m3u8downloader.domain.SystemConfigDao;
import com.github.cloudgyb.m3u8downloader.model.DownloadTask;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author cloudgyb
 * 2021/5/16 18:54
 */
public class ApplicationStore {
    private static final List<DownloadTask> noFinishedTask = new CopyOnWriteArrayList<>();
    private static final String workDir;
    private static final String tmpDir;
    private static final SystemConfig systemConfig;

    static {
        final DownloadTaskDao downloadTaskDao = new DownloadTaskDao();
        final List<DownloadTaskDomain> list = downloadTaskDao.selectNoFinished();
        for (DownloadTaskDomain domain : list) {
            final DownloadTask downloadTask = new DownloadTask(domain);
            noFinishedTask.add(downloadTask);
        }
        String defaultDownloadDir = System.getProperty("user.home") +
                File.separator + "Downloads" + File.separator;
        final SystemConfigDao configDao = new SystemConfigDao();
        SystemConfig config = configDao.select();
        if (config == null) {
            config = new SystemConfig();
            config.setDefaultThreadCount(2);
            config.setDownloadDir(defaultDownloadDir);
        }
        systemConfig = config;
        workDir = System.getProperty("user.dir") + File.separator;
        tmpDir = System.getProperty("java.io.tmpdir");
    }

    public static List<DownloadTask> getNoFinishedTasks() {
        return noFinishedTask;
    }

    public static String getWorkDir() {
        return workDir;
    }

    public static String getTmpDir() {
        return tmpDir;
    }

    public static SystemConfig getSystemConfig() {
        return systemConfig;
    }

}
