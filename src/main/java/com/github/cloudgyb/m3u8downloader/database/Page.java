package com.github.cloudgyb.m3u8downloader.database;

import java.util.ArrayList;

/**
 * 分页结果
 *
 * @param <T> 实体类型
 * @author cloudgyb
 * @since 2025/06/30 14:05
 */
public class Page<T> extends ArrayList<T> {
    private final int pageNum;
    private final int pageSize;
    private final int totalCount;
    private final int totalPage;

    public Page(int pageNum, int pageSize, int totalCount) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = (totalCount + pageSize - 1) / pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

}
