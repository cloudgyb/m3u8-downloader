package com.github.cloudgyb.m3u8downloader.util;

/**
 * 系统工具类
 *
 * @author cloudgyb
 * @since 2025/07/14 10:13
 */
public class OSUtil {
    public static final String OS_NAME = System.getProperty("os.name");
    public static final boolean IS_WINDOWS = OS_NAME.toLowerCase().startsWith("win");
    public static final boolean IS_MAC = OS_NAME.toLowerCase().startsWith("mac");
    public static final boolean IS_LINUX = OS_NAME.toLowerCase().startsWith("linux");
}
