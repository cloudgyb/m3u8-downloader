package com.github.cloudgyb.m3u8downloader.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author cloudgyb
 * @since 2025/6/23 22:15
 */
class DBUtil {
    public static final String DB_URL = "jdbc:sqlite:url_test_history.db";
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";


    public static Page<UrlTestHistory> selectPage(int pageNum, int pageSize) throws SQLException {
        List<UrlTestHistory> list = new ArrayList<>(pageSize);
        long total = 0;
        // count
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(DB_PAGE_COUNT_SQL)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getLong(1);
            }
        }
        // page
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(DB_PAGE_SQL_BY_TEST_TIME_DESC)) {
            ps.setInt(1, pageNum * pageSize);
            ps.setInt(2, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UrlTestHistory urlTestHistory = rowHandler.apply(rs);
                list.add(urlTestHistory);
            }
        }
        return new Page<>(total, pageNum, pageSize, list);
    }

    public static List<UrlTestHistory> selectAll() throws SQLException {
        List<UrlTestHistory> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(DB_SELECT_ALL_SQL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UrlTestHistory urlTestHistory = rowHandler.apply(rs);
                list.add(urlTestHistory);
            }
        }
        return list;
    }

    public static void insert(UrlTestHistory urlTestHistory) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DB_INSERT_SQL)) {
            ps.setString(1, urlTestHistory.getUrl());
            ps.setInt(2, urlTestHistory.getStatusCode());
            ps.setLong(3, urlTestHistory.getLoadTime());
            ps.setTimestamp(4, urlTestHistory.getTestTime());
            ps.setString(5, urlTestHistory.getTestErrorInfo());
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("插入成功");
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void delete(Integer id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DB_DELETE_SQL)) {
            ps.setInt(1, id);
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("删除成功");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




   /* private static final Function<ResultSet, UrlTestHistory> rowHandler = (rs) -> {
        try {
            UrlTestHistory urlTestHistory = new UrlTestHistory();
            urlTestHistory.setId(rs.getInt(DB_COLUMN_ID));
            urlTestHistory.setUrl(rs.getString(DB_COLUMN_URL));
            urlTestHistory.setStatusCode(rs.getInt(DB_COLUMN_STATUS_CODE));
            urlTestHistory.setLoadTime(rs.getLong(DB_COLUMN_LOAD_TIME));
            urlTestHistory.setTestTime(rs.getTimestamp(DB_COLUMN_TEST_TIME));
            urlTestHistory.setTestErrorInfo(rs.getString(DB_COLUMN_TEST_ERROR_INFO));
            return urlTestHistory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };*/
}
