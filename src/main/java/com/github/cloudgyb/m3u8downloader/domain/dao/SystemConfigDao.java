package com.github.cloudgyb.m3u8downloader.domain.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cloudgyb.m3u8downloader.database.DBUtil;
import com.github.cloudgyb.m3u8downloader.domain.entity.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置Dao
 *
 * @author cloudgyb
 * 2021/5/19 9:34
 */
public class SystemConfigDao {
    private static final Logger log = LoggerFactory.getLogger(SystemConfigDao.class);
    private static final String tableName = "system_config";
    private static final String allField = "id,config_json";
    private static final String selectByIdSQL = "select " + allField + " from " + tableName + " where id=?";
    private static final String insertSQL = "insert into " + tableName + "(" + allField + ") values(?,?)";
    private static final String updateSQL = "update " + tableName + " set config_json=? where id=?";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private void insert(SystemConfig systemConfig) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, 1);
            ps.setString(2, objectMapper.writeValueAsString(systemConfig));
            int i = ps.executeUpdate();
            if (i == 1) {
                log.info("insert system config success");
            } else {
                log.warn("insert system config failed");
            }
            resultSet = ps.getGeneratedKeys();
            if (resultSet.next()) {
                final int id = resultSet.getInt(1);
                systemConfig.setId(id);
            }
            connection.commit();
        } catch (SQLException | JsonProcessingException e) {
            if (e instanceof SQLException && connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    log.error("database exception", ex);
                }
            }
            log.error("database exception", e);
            throw new RuntimeException("database exception");
        } finally {
            DBUtil.close(connection, ps, resultSet);
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
            ps.setString(1, objectMapper.writeValueAsString(systemConfig));
            ps.setInt(2, 1);
            int i = ps.executeUpdate();
            if (i == 1) {
                log.info("update system config success");
            } else {
                log.warn("update system config failed");
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException("database exception");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.close(connection, ps, null);
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
            return !systemConfigs.isEmpty() ? systemConfigs.get(0) : null;
        } catch (SQLException e) {
            throw new RuntimeException("database exception");
        } finally {
            DBUtil.close(connection, ps, rs);
        }
    }

    private List<SystemConfig> convert(ResultSet rs) throws SQLException {
        List<SystemConfig> list = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt(1);
            String configJSON = rs.getString(2);
            SystemConfig systemConfig;
            try {
                systemConfig = objectMapper.readValue(configJSON, SystemConfig.class);
            } catch (JsonProcessingException e) {
                log.error("json parse error", e);
                systemConfig = new SystemConfig(); // 使用默认值
            }
            list.add(systemConfig);
        }
        return list;
    }

}
