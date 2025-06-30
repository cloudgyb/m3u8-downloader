package com.github.cloudgyb.m3u8downloader.database;

import com.github.cloudgyb.m3u8downloader.conf.JDBCConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * 数据库工具类
 *
 * @author cloudgyb
 * @since 2025/6/23 22:15
 */
public class DBUtil {
    private static final JDBCConnectionConfiguration connectionConfiguration =
            JDBCConnectionConfiguration.getInstance();
    private static final Logger log = LoggerFactory.getLogger(DBUtil.class);

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(connectionConfiguration.getDbUrl(),
                connectionConfiguration.getDbUser(), connectionConfiguration.getDbPassword());
        connection.setAutoCommit(false);
        return connection;
    }

    public static void close(Connection conn, Statement s, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (s != null)
                s.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            log.error("database exception");
        }
    }
}
