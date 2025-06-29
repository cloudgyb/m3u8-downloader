package com.github.cloudgyb.m3u8downloader.conf;

/**
 * @author cloudgyb
 * @since 2025/6/29 22:11
 */
public class SqliteConfiguration {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private static final SqliteConfiguration instance;

    public static SqliteConfiguration getInstance() {
        if (instance == null) {
            synchronized (SqliteConfiguration.class) {
                if (instance == null) {
                    ApplicationConfig applicationConfig = SpringBeanUtil.getBean(ApplicationConfig.class);
                    instance = new SqliteConfiguration(applicationConfig.getDbFile(), "", "");
                }
            }
        }
        return instance;
    }

    private SqliteConfiguration(String dbUrl, String dbUser, String dbPassword) {
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
