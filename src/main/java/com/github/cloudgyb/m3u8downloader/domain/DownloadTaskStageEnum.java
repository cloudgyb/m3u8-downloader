package com.github.cloudgyb.m3u8downloader.domain;

/**
 * 下载任务生命周期状态枚举
 * <p>
 * 下载任务生命周期分为 5 个阶段：新建->m3u8 索引文件解析->下载->媒体片段合并->完成，
 * 有的阶段可能包含多个状态，但同时只能处于一个状态，具体见下面的枚举值。
 *
 * @author cloudgyb
 * 2021/5/18 15:46
 */
public enum DownloadTaskStageEnum {
    NEW("新建"),
    M3U8_PARSING("m3u8 解析中"),
    M3U8_PARSED("m3u8 已经解析完成"),
    M3U8_PARSE_FAILED("m3u8 解析失败"),
    DOWNLOADING("正在下载中"),
    DOWNLOAD_FAILED("下载失败"),
    DOWNLOAD_FINISHED("下载完成"),
    SEGMENT_MERGING("媒体片段合并中"),
    SEGMENT_MERGED("媒体片段合并完成"),
    SEGMENT_MERGE_FAILED("媒体片段合并失败"),
    STOPPED("手动停止"),
    FINISHED("完成");

    final String status;

    DownloadTaskStageEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static boolean isRunning(DownloadTaskStageEnum statusEnum) {
        return statusEnum != NEW &&
                statusEnum != FINISHED &&
                statusEnum != M3U8_PARSE_FAILED &&
                statusEnum != DOWNLOAD_FAILED;
    }
}
