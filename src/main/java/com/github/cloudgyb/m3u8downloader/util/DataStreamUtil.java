package com.github.cloudgyb.m3u8downloader.util;

import java.io.*;

/**
 * 数据流与文件工具类
 *
 * @author cloudgyb
 * 2021/5/17 16:28
 */
public class DataStreamUtil {
    public static void copy(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) throws IOException {
        byte[] buff = new byte[512];
        int n;
        while ((n = in.read(buff)) > 0) {
            out.write(buff, 0, n);
        }
        if (closeIn)
            in.close();
        if (closeOut)
            out.close();
    }

    public static void copy(File sourceFile, OutputStream out, boolean closeOut) throws IOException {
        final FileInputStream fis = new FileInputStream(sourceFile);
        copy(fis, out, true, closeOut);
    }
}
