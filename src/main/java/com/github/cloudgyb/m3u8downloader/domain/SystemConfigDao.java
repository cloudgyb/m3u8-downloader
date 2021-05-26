package com.github.cloudgyb.m3u8downloader.domain;

import com.github.cloudgyb.m3u8downloader.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置Dao
 *
 * @author cloudgyb
 * 2021/5/19 9:34
 */
public class SystemConfigDao {
    private static final String tableName = "system_config";
    private static final String allField = "id,download_dir,default_thread_count";
    private static final String selectByIdSQL = "select " + allField + " from " + tableName + " where id=?";
    private static final String insertSQL = "insert into " + tableName + "(" + allField + ") values(?,?,?)";
    private static final String updateSQL = "update " + tableName + " set download_dir=?,default_thread_count=? where id=?";

    public void insert(SystemConfig systemConfig) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(insertSQL);
            ps.setInt(1, 1);
            ps.setString(2, systemConfig.getDownloadDir());
            ps.setInt(3, systemConfig.getDefaultThreadCount());
            ps.executeUpdate();
            resultSet = ps.getGeneratedKeys();
            if (resultSet.next()) {
                final int id = resultSet.getInt(1);
                systemConfig.setId(id);
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

    public void update(SystemConfig systemConfig) {
        final SystemConfig config = this.select();
        if (config == null) {
            this.insert(systemConfig);
            return;
        }
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(updateSQL);
            ps.setString(1, systemConfig.getDownloadDir());
            ps.setInt(2, systemConfig.getDefaultThreadCount());
            ps.setInt(3, 1);
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

    public SystemConfig select() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(selectByIdSQL);
            ps.setInt(1, 1);
            rs = ps.executeQuery();
            final List<SystemConfig> systemConfigs = convert(rs);
            return systemConfigs.size() > 0 ? systemConfigs.get(0) : null;
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


    private List<SystemConfig> convert(ResultSet rs) throws SQLException {
        List<SystemConfig> list = new ArrayList<>();
        while (rs.next()) {
            SystemConfig config = new SystemConfig();
            config.setId(rs.getInt(1));
            config.setDownloadDir(rs.getString(2));
            config.setDefaultThreadCount(rs.getInt(3));
            list.add(config);
        }
        return list;
    }

}
