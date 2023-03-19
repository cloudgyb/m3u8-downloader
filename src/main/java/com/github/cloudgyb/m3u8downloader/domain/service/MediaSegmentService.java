package com.github.cloudgyb.m3u8downloader.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.m3u8downloader.domain.entity.MediaSegmentEntity;
import com.github.cloudgyb.m3u8downloader.domain.mapper.MediaSegmentEntityMapper;
import com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author geng
 * @since 2023/03/17 14:00:41
 */
@Service
public class MediaSegmentService extends ServiceImpl<MediaSegmentEntityMapper, MediaSegmentEntity> {

    @Transactional(rollbackFor = Exception.class)
    public void saveAllMediaSegments(int tid, List<MediaSegment> mediaSegments) {
        mediaSegments.stream()
                .map(mediaSegment -> {
                    String url = mediaSegment.getUrl();
                    double duration = mediaSegment.getDuration();
                    MediaSegmentEntity mediaSegmentEntity = new MediaSegmentEntity();
                    mediaSegmentEntity.setTaskId(tid);
                    mediaSegmentEntity.setUrl(url);
                    mediaSegmentEntity.setDuration(Double.valueOf(duration).longValue());
                    mediaSegmentEntity.setFinished(false);
                    return mediaSegmentEntity;
                })
                .forEach(this::save);
    }

    public List<MediaSegmentEntity> getByTaskId(Integer tid, boolean isFinished, int size) {
        LambdaQueryWrapper<MediaSegmentEntity> queryWrapper = new LambdaQueryWrapper<MediaSegmentEntity>()
                .eq(MediaSegmentEntity::getTaskId, tid)
                .eq(MediaSegmentEntity::getFinished, isFinished);
        if (size > 0) {
            queryWrapper.last("limit " + size);
        }
        return list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByTid(Integer tid) {
        remove(
                new LambdaQueryWrapper<MediaSegmentEntity>()
                        .in(MediaSegmentEntity::getTaskId, tid)
        );
    }
}
