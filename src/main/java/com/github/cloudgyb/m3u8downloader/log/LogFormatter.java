package com.github.cloudgyb.m3u8downloader.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * 日志格式化
 *
 * @author cloudgyb
 * 2021/5/22 17:11
 */
public class LogFormatter extends SimpleFormatter {

    @Override
    public String format(LogRecord record) {
        //获取时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date now = new Date();
        String time = sdf.format(now);
        final Level level = record.getLevel();
        final long sequenceNumber = record.getSequenceNumber();
        final int threadID = record.getThreadID();
        final String message = record.getMessage();
        final String sourceClassName = record.getSourceClassName();
        final String sourceMethodName = record.getSourceMethodName();
        return String.format("[%s] ThreadID-%s %s %s.%s,%s: %s\n", level.getName(), threadID,
                time, sourceClassName, sourceMethodName,sequenceNumber, message);
    }
}
