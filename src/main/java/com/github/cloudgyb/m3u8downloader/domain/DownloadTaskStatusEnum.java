package com.github.cloudgyb.m3u8downloader.domain;

/**
 * 下载任务状态枚举，用于在下载列表展示任务状态
 * <p>
 * 该枚举与 {@link DownloadTaskStageEnum} 作用不同，
 * {@link DownloadTaskStageEnum} 用于记录下载处于哪个阶段，而该枚举记录任务的状态
 *
 * @author cloudgyb
 * 2021/5/18 15:46
 */
public enum DownloadTaskStatusEnum {
    NEW("未开始"),
    M3U8_PARSING("m3u8 解析中"),
    M3U8_PARSED("m3u8 已经解析完成"),
    M3U8_PARSE_FAILED("m3u8 解析失败"),
    DOWNLOADING("正在下载中"),
    DOWNLOAD_FINISHED("下载完成"),
    DOWNLOAD_FAILED("下载失败"),
    SEGMENT_MERGING("媒体片段合并中"),
    SEGMENT_MERGED("媒体片段合并完成"),
    SEGMENT_MERGE_FAILED("媒体片段合并失败"),
    STOPPED("停止"),
    FINISHED("完成");

    final String status;

    DownloadTaskStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static boolean isRunning(DownloadTaskStatusEnum statusEnum) {
        return statusEnum != NEW &&
                statusEnum != M3U8_PARSE_FAILED &&
                statusEnum != DOWNLOAD_FAILED &&
                statusEnum != SEGMENT_MERGE_FAILED &&
                statusEnum != STOPPED &&
                statusEnum != FINISHED;

    }

    public static boolean isFailed(DownloadTaskStatusEnum statusEnum) {
        return statusEnum == M3U8_PARSE_FAILED ||
                statusEnum == DOWNLOAD_FAILED ||
                statusEnum == SEGMENT_MERGE_FAILED;
    }
}
