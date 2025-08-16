package com.github.cloudgyb.m3u8downloader.domain;

/**
 * 下载任务生命周期状态枚举
 * <p>
 * 下载任务生命周期分为 5 个阶段：新建->m3u8 索引文件解析->下载->媒体片段合并->完成
 *
 * @author cloudgyb
 * 2021/5/18 15:46
 */
public enum DownloadTaskStageEnum {
    NEW("新建"),
    M3U8_PARSED("m3u8 已经解析完成"),
    DOWNLOAD_FINISHED("所有媒体片段下载完成"),
    SEGMENT_MERGED("媒体片段合并完成"),
    FINISHED("完成");

    final String status;

    DownloadTaskStageEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
