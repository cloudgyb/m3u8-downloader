package com.github.cloudgyb.m3u8downloader.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.mapper.DownloadTaskEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author geng
 * @since 2023/03/17 14:03:08
 */
@Service
public class DownloadTaskService extends ServiceImpl<DownloadTaskEntityMapper, DownloadTaskEntity> {
    private final MediaSegmentService mediaSegmentService;

    public DownloadTaskService(MediaSegmentService mediaSegmentService) {
        this.mediaSegmentService = mediaSegmentService;
    }

    /**
     * 从数据库中查询出未完成的下载任务
     */
    public List<DownloadTaskEntity> getAllNotFinishedTask() {
        return list(
                new LambdaQueryWrapper<DownloadTaskEntity>()
                        .ne(DownloadTaskEntity::getStage, DownloadTaskStageEnum.FINISHED)
        );
    }

    public List<DownloadTaskEntity> getAllFinishedTask() {
        return list(
                new LambdaQueryWrapper<DownloadTaskEntity>()
                        .eq(DownloadTaskEntity::getStage, DownloadTaskStageEnum.FINISHED)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        mediaSegmentService.deleteByTid(id);
        removeById(id);
    }
}
