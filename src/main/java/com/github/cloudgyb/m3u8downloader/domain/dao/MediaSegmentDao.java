package com.github.cloudgyb.m3u8downloader.domain.dao;


import com.github.cloudgyb.m3u8downloader.database.DBUtil;
import com.github.cloudgyb.m3u8downloader.database.Page;
import com.github.cloudgyb.m3u8downloader.domain.entity.MediaSegmentEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 媒体分片信息存储数据库访问对象
 *
 * @author cloudgyb
 * @since 2025/06/30 14:14:08
 */
public class MediaSegmentDao implements IDao<MediaSegmentEntity, Integer> {
    private static final MediaSegmentDao instance = new MediaSegmentDao();

    private MediaSegmentDao() {
    }

    public static MediaSegmentDao getInstance() {
        return instance;
    }

    private static final String insertSQL = "insert into media_segment" +
            "(task_id,url,duration,download_duration,finished,file_path)" +
            " values(?,?,?,?,?,?)";

    @Override
    public int insert(MediaSegmentEntity entity) {
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
                ps.setInt(1, entity.getTaskId());
                ps.setString(2, entity.getUrl());
                ps.setLong(3, entity.getDuration());
                ps.setLong(4, entity.getDownloadDuration() == null ? 0 : entity.getDownloadDuration());
                ps.setBoolean(5, entity.getFinished() != null && entity.getFinished());
                ps.setString(6, entity.getFilePath() == null ? "" : entity.getFilePath());
                int i = ps.executeUpdate();
                connection.commit();
                return i;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String updateSQL = "update media_segment" +
            " set task_id = ?,url = ?,duration = ?,download_duration = ?,finished = ?,file_path = ?" +
            " where id = ?";

    @Override
    public int updateById(MediaSegmentEntity entity) {
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(updateSQL)) {
                ps.setInt(1, entity.getTaskId());
                ps.setString(2, entity.getUrl());
                ps.setLong(3, entity.getDuration());
                ps.setLong(4, entity.getDownloadDuration());
                ps.setBoolean(5, entity.getFinished());
                ps.setString(6, entity.getFilePath());
                ps.setInt(7, entity.getId());
                int i = ps.executeUpdate();
                connection.commit();
                return i;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final static String deleteSQL = "delete from media_segment where id = ?";

    @Override
    public int deleteById(Integer id) {
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
                ps.setInt(1, id);
                int i = ps.executeUpdate();
                connection.commit();
                return i;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String selectByIdSQL = "select * from media_segment where id = ?";

    @Override
    public MediaSegmentEntity selectById(Integer id) {
        MediaSegmentEntity res = null;
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(selectByIdSQL)) {
                ps.setInt(1, id);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    res = rowHandler.apply(resultSet);
                }
                resultSet.close();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private static final String selectAllSQL = "select * from media_segment";

    @Override
    public List<MediaSegmentEntity> selectAll() {
        ArrayList<MediaSegmentEntity> list = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(selectAllSQL);
                 ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    MediaSegmentEntity mediaSegmentEntity = rowHandler.apply(resultSet);
                    list.add(mediaSegmentEntity);
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    private static final String selectEqTaskIdAndEqFinishedSQL =
            "select * from media_segment where task_id=? and finished=?";
    private static final String selectEqTaskIdAndEqFinishedAndLimitSQL =
            "select * from media_segment where task_id=? and finished=? limit ?";

    public List<MediaSegmentEntity> selectByTaskIdAndFinished(Integer tid, boolean isFinished, int size) {
        ArrayList<MediaSegmentEntity> list = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection()) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        size > 0 ? selectEqTaskIdAndEqFinishedAndLimitSQL : selectEqTaskIdAndEqFinishedSQL
                );
                ps.setInt(1, tid);
                ps.setBoolean(2, isFinished);
                if (size > 0) {
                    ps.setInt(3, size);
                }
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    MediaSegmentEntity mediaSegmentEntity = rowHandler.apply(resultSet);
                    list.add(mediaSegmentEntity);
                }
                resultSet.close();
                ps.close();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private static final String selectPageCountTotalSQL = "select count(*) from media_segment";
    private static final String selectPageSQL = "select * from media_segment limit ?,?";

    @Override
    public Page<MediaSegmentEntity> selectPage(int pageNum, int pageSize) {
        Page<MediaSegmentEntity> page;
        int total = 0;
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(selectPageCountTotalSQL);
                 PreparedStatement ps1 = connection.prepareStatement(selectPageSQL)) {
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    total = resultSet.getInt(1);
                }
                resultSet.close();
                page = new Page<>(total, pageNum, pageSize);
                if (total == 0) {
                    return page;
                }
                ps1.setInt(1, (pageNum - 1) * pageSize);
                ps1.setInt(2, pageSize);
                resultSet = ps1.executeQuery();
                while (resultSet.next()) {
                    MediaSegmentEntity mediaSegmentEntity = rowHandler.apply(resultSet);
                    page.add(mediaSegmentEntity);
                }
                resultSet.close();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return page;
    }

    private static final String deleteByTaskIdSQL = "delete from media_segment where task_id = ?";

    public int deleteByTaskId(Integer taskId) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteByTaskIdSQL)) {
            ps.setInt(1, taskId);
            int i = ps.executeUpdate();
            connection.commit();
            return i;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Function<ResultSet, MediaSegmentEntity> rowHandler = resultSet -> {
        MediaSegmentEntity mediaSegmentEntity = new MediaSegmentEntity();
        try {
            mediaSegmentEntity.setId(resultSet.getInt("id"));
            mediaSegmentEntity.setTaskId(resultSet.getInt("task_id"));
            mediaSegmentEntity.setUrl(resultSet.getString("url"));
            mediaSegmentEntity.setFinished(resultSet.getBoolean("finished"));
            mediaSegmentEntity.setDuration(resultSet.getLong("duration"));
            mediaSegmentEntity.setDownloadDuration(resultSet.getLong("download_duration"));
            mediaSegmentEntity.setFilePath(resultSet.getString("file_path"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mediaSegmentEntity;
    };
}
