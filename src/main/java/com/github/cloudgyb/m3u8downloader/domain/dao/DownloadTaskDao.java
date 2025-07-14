package com.github.cloudgyb.m3u8downloader.domain.dao;

import com.github.cloudgyb.m3u8downloader.database.DBUtil;
import com.github.cloudgyb.m3u8downloader.database.Page;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 下载任务数据访问对象
 *
 * @author cloudgyb
 * @since 2025/06/30 14:12:18
 */
public class DownloadTaskDao implements IDao<DownloadTaskEntity, Integer> {
    private static final DownloadTaskDao instance = new DownloadTaskDao();

    private DownloadTaskDao() {
    }

    public static DownloadTaskDao getInstance() {
        return instance;
    }

    private final static String insertSQL = "insert into task (" +
            " url, create_time, finished_time, total_media_segment," +
            " finish_media_segment, download_duration," +
            " file_path, resolution, max_thread_count, stage, status, save_filename)" +
            "values (?,?,?,?,?,?,?,?,?,?,?,?);";

    @Override
    public int insert(DownloadTaskEntity entity) {
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getUrl());
                ps.setDate(2, new Date(entity.getCreateTime().getTime()));
                ps.setDate(3, entity.getFinishedTime() == null ? null : new Date(entity.getFinishedTime().getTime()));
                ps.setInt(4, entity.getTotalMediaSegment());
                ps.setInt(5, entity.getFinishMediaSegment());
                ps.setLong(6, entity.getDownloadDuration());
                ps.setString(7, entity.getFilePath());
                ps.setString(8, entity.getResolution());
                ps.setInt(9, entity.getMaxThreadCount());
                ps.setString(10, entity.getStage());
                ps.setString(11, entity.getStatus());
                ps.setString(12, entity.getSaveFilename());
                int i = ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
                generatedKeys.close();
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

    private final static String updateSQL = "update task set " +
            " url = ?, finished_time = ?, total_media_segment = ?," +
            " finish_media_segment = ?, download_duration = ?," +
            " file_path = ?, resolution = ?, max_thread_count = ?, stage = ?, status = ?, save_filename = ? " +
            "where id = ?;";

    @Override
    public int updateById(DownloadTaskEntity entity) {
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(updateSQL)) {
                ps.setString(1, entity.getUrl());
                ps.setDate(2, entity.getFinishedTime() == null ? null : new Date(entity.getFinishedTime().getTime()));
                ps.setInt(3, entity.getTotalMediaSegment());
                ps.setInt(4, entity.getFinishMediaSegment());
                ps.setLong(5, entity.getDownloadDuration());
                ps.setString(6, entity.getFilePath());
                ps.setString(7, entity.getResolution());
                ps.setInt(8, entity.getMaxThreadCount());
                ps.setString(9, entity.getStage());
                ps.setString(10, entity.getStatus());
                ps.setString(11, entity.getSaveFilename());
                ps.setInt(12, entity.getId());
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

    private final static String updateSaveFilenameByIdSQL = "update task set " +
            "save_filename = ? " +
            "where id = ?;";

    public int updateSaveFilenameById(Integer id, String newSaveFilename) {
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(updateSaveFilenameByIdSQL)) {
                ps.setString(1, newSaveFilename);
                ps.setInt(2, id);
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

    private final static String deleteSQL = "delete from task where id = ?;";

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

    private static final String selectByIdSQL = "select * from task where id = ?;";

    @Override
    public DownloadTaskEntity selectById(Integer id) {
        DownloadTaskEntity res = null;
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

    private static final String selectAllSQL = "select * from task;";

    @Override
    public List<DownloadTaskEntity> selectAll() {
        ArrayList<DownloadTaskEntity> list = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(selectAllSQL);
                 ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    DownloadTaskEntity downloadTaskEntity = rowHandler.apply(resultSet);
                    list.add(downloadTaskEntity);
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

    private static final String selectStatusSQL = "select * from task where status = ?;";
    private static final String selectNeStatusSQL = "select * from task where status != ?;";

    public List<DownloadTaskEntity> selectByStatus(String statusName, boolean eq) {
        ArrayList<DownloadTaskEntity> list = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection()) {
            try {
                PreparedStatement ps = connection.prepareStatement(eq ? selectStatusSQL : selectNeStatusSQL);
                ps.setString(1, statusName);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    DownloadTaskEntity downloadTaskEntity = rowHandler.apply(resultSet);
                    list.add(downloadTaskEntity);
                }
                resultSet.close();
                ps.close();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private static final String selectPageCountTotalByEqStatusSQL = "select count(*) from task where status = ?;";
    private static final String selectPageCountTotalByNeqStatusSQL = "select count(*) from task where status != ?;";
    private static final String selectPageByEqStatusSQL = "select * from task where status = ? order by finished_time desc limit ?,?;";
    private static final String selectPageByNeqStatusSQL = "select * from task where status != ? order by finished_time desc limit ?,?;";

    public Page<DownloadTaskEntity> selectPage(int pageNum, int pageSize, String statusName, boolean eq) {
        Page<DownloadTaskEntity> page;
        int total = 0;
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(eq ? selectPageCountTotalByEqStatusSQL : selectPageCountTotalByNeqStatusSQL);
                 PreparedStatement ps1 = connection.prepareStatement(eq ? selectPageByEqStatusSQL : selectPageByNeqStatusSQL)) {
                ps.setString(1, statusName);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    total = resultSet.getInt(1);
                }
                resultSet.close();
                page = new Page<>(pageNum, pageSize, total);
                if (total == 0) {
                    return page;
                }
                ps1.setString(1, statusName);
                ps1.setInt(2, pageNum * pageSize);
                ps1.setInt(3, pageSize);
                resultSet = ps1.executeQuery();
                while (resultSet.next()) {
                    DownloadTaskEntity downloadTaskEntity = rowHandler.apply(resultSet);
                    page.add(downloadTaskEntity);
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

    private static final String selectPageCountTotalSQL = "select count(*) from task;";
    private static final String selectPageSQL = "select * from task where status = ? order by finished_time desc limit ?,?;";

    @Override
    public Page<DownloadTaskEntity> selectPage(int pageNum, int pageSize) {
        Page<DownloadTaskEntity> page;
        int total = 0;
        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(selectPageCountTotalSQL);
                 PreparedStatement ps1 = connection.prepareStatement(selectPageSQL)) {
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    total = resultSet.getInt(1);
                }
                resultSet.close();
                page = new Page<>(pageNum, pageSize, total);
                if (total == 0) {
                    return page;
                }
                ps1.setInt(1, (pageNum - 1) * pageSize);
                ps1.setInt(2, pageSize);
                resultSet = ps1.executeQuery();
                while (resultSet.next()) {
                    DownloadTaskEntity downloadTaskEntity = rowHandler.apply(resultSet);
                    page.add(downloadTaskEntity);
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

    private static final Function<ResultSet, DownloadTaskEntity> rowHandler = resultSet -> {
        DownloadTaskEntity downloadTaskEntity = new DownloadTaskEntity();
        try {
            downloadTaskEntity.setId(resultSet.getInt("id"));
            downloadTaskEntity.setUrl(resultSet.getString("url"));
            downloadTaskEntity.setCreateTime(resultSet.getDate("create_time"));
            downloadTaskEntity.setFinishedTime(resultSet.getDate("finished_time"));
            downloadTaskEntity.setTotalMediaSegment(resultSet.getInt("total_media_segment"));
            downloadTaskEntity.setFinishMediaSegment(resultSet.getInt("finish_media_segment"));
            downloadTaskEntity.setDownloadDuration(resultSet.getLong("download_duration"));
            downloadTaskEntity.setFilePath(resultSet.getString("file_path"));
            downloadTaskEntity.setResolution(resultSet.getString("resolution"));
            downloadTaskEntity.setMaxThreadCount(resultSet.getInt("max_thread_count"));
            downloadTaskEntity.setStage(resultSet.getString("stage"));
            downloadTaskEntity.setStatus(resultSet.getString("status"));
            downloadTaskEntity.setSaveFilename(resultSet.getString("save_filename"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return downloadTaskEntity;
    };
}
