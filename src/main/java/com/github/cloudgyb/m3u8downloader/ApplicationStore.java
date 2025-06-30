package com.github.cloudgyb.m3u8downloader;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStatusEnum;
import com.github.cloudgyb.m3u8downloader.domain.entity.SystemConfig;
import com.github.cloudgyb.m3u8downloader.domain.dao.SystemConfigDao;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.download.TaskDownloadThreadManager;
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
    private final static DownloadTaskService downloadTaskService = DownloadTaskService.getInstance();

    static {
        List<DownloadTaskEntity> list = downloadTaskService.getAllNotFinishedTask();
        for (DownloadTaskEntity task : list) {
            DownloadTaskStatusEnum statusEnum = DownloadTaskStatusEnum.STOPPED_ERROR;
            // 程序启动初始化下载任务，设置状态为 STOPPED
            String status = task.getStatus();
            if (status != null) {
                statusEnum = DownloadTaskStatusEnum.valueOf(status);
                if (statusEnum == DownloadTaskStatusEnum.RUNNING) {
                    statusEnum = DownloadTaskStatusEnum.STOPPED_ERROR;
                }
            }
            task.setStatus(statusEnum.name());
            final DownloadTaskViewModel downloadTaskViewModel = new DownloadTaskViewModel(task);
            noFinishedTask.add(downloadTaskViewModel);
            TaskDownloadThreadManager.getInstance().createDownloadThread(task);
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

    @SuppressWarnings("unused")
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
