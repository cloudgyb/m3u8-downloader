package com.github.cloudgyb.m3u8downloader.domain.dao;

import com.github.cloudgyb.m3u8downloader.database.Page;

import java.util.List;

/**
 * dao接口
 *
 * @author cloudgyb
 * @since 2025/06/30 14:00
 */
public interface IDao<E, ID> {
    int insert(E entity);

    int updateById(E entity);

    int deleteById(ID id);

    E selectById(ID id);

    List<E> selectAll();

    Page<E> selectPage(int pageNum, int pageSize);
}
