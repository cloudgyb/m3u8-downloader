package com.github.cloudgyb.m3u8downloader.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志输出格式化器
 *
 * @author cloudgyb
 * 2021/5/19 17:41
 */
public class DateFormatter {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        final String format = formatter.format(new Date());
        System.out.println(format);
    }

    public static String format(Date date) {
        return formatter.format(date);
    }
}
