package com.github.cloudgyb.m3u8downloader.domain.service;

import com.github.cloudgyb.m3u8downloader.domain.dao.MediaSegmentDao;
import com.github.cloudgyb.m3u8downloader.domain.entity.MediaSegmentEntity;
import com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 媒体分段服务
 *
 * @author cloudgyb
 * @since 2023/03/17 14:00:41
 */
public class MediaSegmentService {
    private final Logger log = LoggerFactory.getLogger(MediaSegmentService.class);
    private static final MediaSegmentService instance = new MediaSegmentService();
    private final MediaSegmentDao mediaSegmentDao = MediaSegmentDao.getInstance();

    public static MediaSegmentService getInstance() {
        return instance;
    }

    public void save(MediaSegmentEntity entity) {
        int insert = mediaSegmentDao.insert(entity);
        if (insert != 1) {
            log.error("保存分段失败：{}", entity);
        }
    }

    /**
     * 保存所有媒体分段
     *
     * @param taskId        任务id
     * @param mediaSegments 媒体分段
     */
    public void saveAllMediaSegments(int taskId, List<MediaSegment> mediaSegments) {
        mediaSegments.stream()
                .map(mediaSegment -> {
                    String url = mediaSegment.getUrl();
                    double duration = mediaSegment.getDuration();
                    MediaSegmentEntity mediaSegmentEntity = new MediaSegmentEntity();
                    mediaSegmentEntity.setTaskId(taskId);
                    mediaSegmentEntity.setUrl(url);
                    mediaSegmentEntity.setDuration(Double.valueOf(duration).longValue());
                    mediaSegmentEntity.setFinished(false);
                    return mediaSegmentEntity;
                })
                .forEach(this::save);
    }

    public List<MediaSegmentEntity> getByTaskId(Integer tid, boolean isFinished, int size) {
        return mediaSegmentDao.selectByTaskIdAndFinished(tid, isFinished, size);
    }

    public int deleteByTaskId(Integer tid) {
        return mediaSegmentDao.deleteByTaskId(tid);
    }

    public void updateById(MediaSegmentEntity mediaSegmentEntity) {
        int i = mediaSegmentDao.updateById(mediaSegmentEntity);
        if (i != 1) {
            log.error("更新媒体分段失败：{}", mediaSegmentEntity);
        }
    }
}
