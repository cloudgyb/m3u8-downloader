package com.github.cloudgyb.m3u8downloader.viewcontroller;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDao;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 新下载任务模型：新任务下载业务逻辑处理，保存任务信息到数据库
 *
 * @author cloudgyb
 * @since 2.0.0
 */
public class NewDownloadTaskModel {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DownloadTaskDao taskDao = new DownloadTaskDao();

    /**
     * 保存下载任务信息到数据库种
     *
     * @param downloadTaskDomain 下载任务 domain
     */
    public void save(DownloadTaskDomain downloadTaskDomain) {
        taskDao.insert(downloadTaskDomain);
        if (logger.isInfoEnabled()) {
            logger.info("新下载任务已保存数据库！");
        }
    }
}
