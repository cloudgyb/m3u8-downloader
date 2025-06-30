package com.github.cloudgyb.m3u8downloader.conf;

import java.util.Properties;

/**
 * 数据库连接配置
 *
 * @author cloudgyb
 * @since 2025/6/29 22:11
 */
public class JDBCConnectionConfiguration {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private static volatile JDBCConnectionConfiguration instance;

    public static JDBCConnectionConfiguration getInstance() {
        if (instance == null) {
            synchronized (JDBCConnectionConfiguration.class) {
                if (instance == null) {
                    Properties properties = new Properties();
                    try {
                        properties.load(JDBCConnectionConfiguration.class.getClassLoader()
                                .getResourceAsStream("/application.properties"));
                    } catch (Exception e) {
                        throw new RuntimeException("未找到 application.properties 文件！");
                    }
                    String dbUrl = properties.getProperty("db.url");
                    String dbUsername = properties.getProperty("db.username");
                    String dbPassword = properties.getProperty("db.password");
                    instance = new JDBCConnectionConfiguration(dbUrl, dbUsername, dbPassword);
                }
            }
        }
        return instance;
    }

    private JDBCConnectionConfiguration(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

}
