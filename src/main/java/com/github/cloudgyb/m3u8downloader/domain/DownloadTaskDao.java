package com.github.cloudgyb.m3u8downloader.domain;

import com.github.cloudgyb.m3u8downloader.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 下载任务DAO
 *
 * @author cloudgyb
 * 2021/5/18 11:29
 */
public class DownloadTaskDao {
    private static final String tableName = "download_task";
    private static final String insertField = "url,progress,url_total,url_finished,urls," +
            "thread_count,status,create_time,duration";
    private static final String allFields = "id," + insertField + ",finish_time,file_path";
    private static final String insertSQL = "insert into " + tableName + "(" + insertField + ") values(?,?,?,?,?,?,?,?,?)";
    private static final String updateSQL = "update " + tableName + " set url=?,progress=?,url_total=?,url_finished=?,urls=?," +
            "thread_count=?,status=?,create_time=?,duration=?,finish_time=?,file_path=? where id=?";

    public void insert(DownloadTaskDomain task) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(insertSQL);
            ps.setString(1, task.getUrl());
            ps.setDouble(2, task.getProgress());
            ps.setInt(3, task.getUrlTotal());
            ps.setInt(4, 0);
            ps.setString(5, "");
            ps.setInt(6, task.getThreadCount());
            ps.setInt(7, task.getStatus());
            long time = task.getCreateTime() == null ? System.currentTimeMillis() : task.getCreateTime().getTime();
            ps.setLong(8, time);
            ps.setLong(9, 0);
            final int i = ps.executeUpdate();
            System.out.println(i);
            resultSet = ps.getGeneratedKeys();
            if (resultSet.next()) {
                final int id = resultSet.getInt(1);
                task.setId(id);
                System.out.println(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("database exception");
        } finally {
            try {
                DBUtil.close(connection, ps, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(DownloadTaskDomain task) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(updateSQL);
            ps.setString(1, task.getUrl());
            ps.setDouble(2, task.getProgress());
            ps.setInt(3, task.getUrlTotal());
            ps.setInt(4, task.getUrlFinished());
            ps.setString(5, task.getUrls());
            ps.setInt(6, task.getThreadCount());
            ps.setInt(7, task.getStatus());
            long createTime = task.getCreateTime() == null ? System.currentTimeMillis() : task.getCreateTime().getTime();
            ps.setLong(8, createTime);
            ps.setLong(9, task.getDuration());
            long finishTime = task.getFinishTime() == null ? 0 : task.getFinishTime().getTime();
            ps.setLong(10, finishTime);
            ps.setString(11, task.getFilePath());
            ps.setInt(12, task.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("database exception");
        } finally {
            try {
                DBUtil.close(connection, ps, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<DownloadTaskDomain> selectAll() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String selectByIdSQL = "select " + allFields + " from download_task";
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(selectByIdSQL);
            rs = ps.executeQuery();
            return convert(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("database exception");
        } finally {
            try {
                DBUtil.close(connection, ps, rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public DownloadTaskDomain selectById(int id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String selectByIdSQL = "select " + allFields + " from download_task where id=?";
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(selectByIdSQL);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            final List<DownloadTaskDomain> list = convert(rs);
            return list.size() > 0 ? list.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("database exception");
        } finally {
            try {
                DBUtil.close(connection, ps, rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public List<DownloadTaskDomain> selectByStatus(int status) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String selectByIdSQL = "select " + allFields + " from download_task where status=?";
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(selectByIdSQL);
            ps.setInt(1, status);
            rs = ps.executeQuery();
            return convert(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("database exception");
        } finally {
            try {
                DBUtil.close(connection, ps, rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<DownloadTaskDomain> selectNoFinished() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String selectByIdSQL = "select " + allFields + " from download_task where status!=?";
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(selectByIdSQL);
            ps.setInt(1, DownloadTaskStatusEnum.FINISHED.getStatus());
            rs = ps.executeQuery();
            return convert(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("database exception");
        } finally {
            try {
                DBUtil.close(connection, ps, rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<DownloadTaskDomain> convert(ResultSet rs) throws SQLException {
        List<DownloadTaskDomain> list = new ArrayList<>();
        while (rs.next()) {
            DownloadTaskDomain t = new DownloadTaskDomain();
            t.setId(rs.getInt(1));
            t.setUrl(rs.getString(2));
            t.setProgress(rs.getDouble(3));
            t.setUrlTotal(rs.getInt(4));
            t.setUrlFinished(rs.getInt(5));
            t.setUrls(rs.getString(6));
            t.setThreadCount(rs.getInt(7));
            t.setStatus(rs.getInt(8));
            t.setCreateTime(new Date(rs.getLong(9)));
            t.setDuration(rs.getLong(10));
            t.setFinishTime(new Date(rs.getLong(11)));
            t.setFilePath(rs.getString(12));
            list.add(t);
        }
        return list;
    }

    public void deleteById(int id) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            String deleteSQL = "delete from " + tableName + " where id=?";
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(deleteSQL);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("database exception");
        } finally {
            try {
                DBUtil.close(connection, ps, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
