package com.github.cloudgyb.m3u8downloader.crx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 浏览器与本地应用通信 Native message 工具类
 *
 * @author geng
 * @since 2024/12/19 15:10:56
 */
public class NativeMessage {
    private static final Logger log = LoggerFactory.getLogger(NativeMessage.class);

    public static String nativeMsgRead() {
        try {
            return readMessageFromStdin();
        } catch (IOException e) {
            return null;
        }
    }


    private static String readMessageFromStdin() throws IOException {
        byte[] b = new byte[4];
        int read = System.in.read(b);// Read the size of message
        if (log.isDebugEnabled()) {
            log.debug("read bytes from stdin size: {}", read);
        }
        int size = getMessageSize(b);

        if (size == 0) {
            return null;
        }

        b = new byte[size];
        size = System.in.read(b);
        if (size == 0) {
            return null;
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    private static void writeMessageToStdout(String message) throws IOException {
        System.out.write(intToBytes(message.length()));
        System.out.write(message.getBytes(StandardCharsets.UTF_8));
        System.out.flush();
    }

    public static int getMessageSize(byte[] bytes) {
        return (bytes[3] << 24) & 0xff000000 | (bytes[2] << 16) & 0x00ff0000 | (bytes[1] << 8) & 0x0000ff00
                | (bytes[0]) & 0x000000ff;
    }

    public static byte[] intToBytes(int length) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (length & 0xFF);
        bytes[1] = (byte) ((length >> 8) & 0xFF);
        bytes[2] = (byte) ((length >> 16) & 0xFF);
        bytes[3] = (byte) ((length >> 24) & 0xFF);
        return bytes;
    }
}
