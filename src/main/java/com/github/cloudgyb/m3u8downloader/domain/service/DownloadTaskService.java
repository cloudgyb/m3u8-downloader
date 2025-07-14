package com.github.cloudgyb.m3u8downloader.domain.service;

import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.dao.DownloadTaskDao;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskHistoryViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 下载任务服务
 *
 * @author cloudgyb
 * @since 2025/06/30 15:50:08
 */
public class DownloadTaskService {
    private static final Logger log = LoggerFactory.getLogger(DownloadTaskService.class);
    private static final DownloadTaskService instance = new DownloadTaskService(MediaSegmentService.getInstance());

    private final DownloadTaskDao downloadTaskDao = DownloadTaskDao.getInstance();
    private final MediaSegmentService mediaSegmentService;

    public DownloadTaskService(MediaSegmentService mediaSegmentService) {
        this.mediaSegmentService = mediaSegmentService;
    }

    public static DownloadTaskService getInstance() {
        return instance;
    }

    public void save(DownloadTaskEntity entity) {
        int insert = downloadTaskDao.insert(entity);
        if (insert != 1) {
            log.error("保存任务失败：{}", entity);
        }
    }

    /**
     * 从数据库中查询出未完成的下载任务
     */
    public List<DownloadTaskEntity> getAllNotFinishedTask() {
        return downloadTaskDao.selectByStatus(DownloadTaskStageEnum.FINISHED.name(), false);
    }

    public List<DownloadTaskHistoryViewModel> getAllFinishedTask() {
        List<DownloadTaskEntity> downloadTaskEntities = downloadTaskDao.selectByStatus(DownloadTaskStageEnum.FINISHED.name(), true);
        return downloadTaskEntities.stream().map(DownloadTaskHistoryViewModel::new).collect(Collectors.toList());
    }

    public void deleteById(Integer id) {
        int i1 = mediaSegmentService.deleteByTaskId(id);
        if (i1 == 0) {
            log.warn("删除任务id:{}的媒体片段可能失败！", id);
        }
        int i = downloadTaskDao.deleteById(id);
        if (i != 1) {
            log.error("删除任务失败id:{}", id);
        }
    }

    public void updateById(DownloadTaskEntity task) {
        int i = downloadTaskDao.updateById(task);
        if (i != 1) {
            log.error("更新任务失败：{}", task);
        }
    }

    public void updateSaveFilename(Integer id, String newSaveFilename) {
        int i = downloadTaskDao.updateSaveFilenameById(id, newSaveFilename);
        if (i != 1) {
            log.error("更新任务保存文件名失败：id:{}", id);
        }
    }
}
