package com.github.cloudgyb.m3u8downloader.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据库初始化
 *
 * @author cloudgyb
 * @since 2025/6/29 21:47
 */
public final class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final String SQL_FILE = "/db.sql";

    public static void initDatabase() {
        String sql;
        try (InputStream inputStream = DatabaseInitializer.class.getResourceAsStream(SQL_FILE)) {
            if (inputStream == null) {
                logger.error("未找到初始化 sql 文件！");
                throw new RuntimeException("未找到初始化 sql 文件！");
            }
            byte[] bytes = inputStream.readAllBytes();
            sql = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("读取初始化 sql 文件失败！", e);
            return;
        }
        try (Connection connection = DBUtil.getConnection()) {
            String[] sqls = sql.split(";");
            for (String s : sqls) {
                s = s.trim();
                try (PreparedStatement ps = connection.prepareStatement(s)) {
                    boolean isSuccess = ps.execute();
                    if (isSuccess) {
                        logger.info("表结构已初始化！");
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("执行初始化 SQL 出错！{}", e.getMessage());
        }
    }
}
