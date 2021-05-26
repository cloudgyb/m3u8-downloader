package com.github.cloudgyb.m3u8downloader.util;

import java.sql.*;

/**
 * 数据库工具类
 *
 * @author cloudgyb
 * 2021/5/17 21:41
 */
public class DBUtil {
    private final static String JDBC_URL = "jdbc:sqlite:data.db";
    private final static String TABLE_SCHEMA_DOWNLOAD_TASK = "CREATE TABLE  IF NOT EXISTS \"download_task\" (" +
            "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"url\" TEXT," +
            "  \"progress\" real," +
            "  \"url_total\" integer," +
            "  \"url_finished\" integer," +
            "  \"urls\" TEXT," +
            "  \"thread_count\" integer," +
            "  \"status\" integer," +
            "  \"create_time\" integer," +
            "  \"duration\" integer," +
            "  \"finish_time\" integer," +
            "  \"file_path\" TEXT" +
            ");";
    private final static String TABLE_SCHEMA_SYSTEM_CONFIG = "CREATE TABLE  IF NOT EXISTS \"system_config\" (" +
            "  \"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"download_dir\" TEXT," +
            "  \"default_thread_count\" TEXT" +
            ");";

    static {
        Connection connection = null;
        Statement ps = null;
        try {
            connection = DBUtil.getConnection();
            ps = connection.createStatement();
            ps.executeUpdate(TABLE_SCHEMA_DOWNLOAD_TASK);
            ps.executeUpdate(TABLE_SCHEMA_SYSTEM_CONFIG);
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

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    public static void close(Connection conn, Statement s, ResultSet rs) throws SQLException {
        if (rs != null)
            rs.close();
        if (s != null)
            s.close();
        if (conn != null)
            conn.close();
    }

}
