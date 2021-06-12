package com.github.cloudgyb.m3u8downloader;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDao;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import com.github.cloudgyb.m3u8downloader.domain.SystemConfig;
import com.github.cloudgyb.m3u8downloader.domain.SystemConfigDao;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author cloudgyb
 * 2021/5/16 18:54
 */
public class ApplicationStore {
    private static final List<DownloadTaskViewModel> noFinishedTask = new CopyOnWriteArrayList<>();
    private static final String workDir;
    private static final String tmpDir;
    private static volatile SystemConfig systemConfig;

    static {
        final DownloadTaskDao downloadTaskDao = new DownloadTaskDao();
        final List<DownloadTaskDomain> list = downloadTaskDao.selectNoFinished();
        for (DownloadTaskDomain domain : list) {
            final DownloadTaskViewModel downloadTaskViewModel = new DownloadTaskViewModel(domain);
            noFinishedTask.add(downloadTaskViewModel);
        }
        //初始化系统配置
        String defaultDownloadDir = System.getProperty("user.home") +
                File.separator + "Downloads" + File.separator;
        final SystemConfigDao configDao = new SystemConfigDao();
        systemConfig = configDao.select();
        if (systemConfig == null) {
            systemConfig = new SystemConfig();
            systemConfig.setDefaultThreadCount(2);
            systemConfig.setDownloadDir(defaultDownloadDir);
        }
        if (systemConfig.getDownloadDir() == null || "".equals(systemConfig.getDownloadDir())) {
            systemConfig.setDownloadDir(defaultDownloadDir);
        }
        systemConfig.setDefaultTimeoutRetryCount(5);
        //
        workDir = System.getProperty("user.dir") + File.separator;
        tmpDir = System.getProperty("java.io.tmpdir");
    }

    public static List<DownloadTaskViewModel> getNoFinishedTasks() {
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
