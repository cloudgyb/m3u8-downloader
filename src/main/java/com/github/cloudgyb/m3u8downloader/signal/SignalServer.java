package com.github.cloudgyb.m3u8downloader.signal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 信号通知服务器
 * <p>
 * 监听 TCP 端口，接收命令，并交给 SignalHandler 处理命令。
 * </p>
 */
public class SignalServer {
    private static final Logger log = LoggerFactory.getLogger(SignalServer.class);
    private volatile Boolean isRunning = false;
    private final ServerSocket serverSocket;
    private final List<SignalHandler> handlers = new ArrayList<>();

    public SignalServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public SignalServer signalHandler(SignalHandler... handlers) {
        this.handlers.addAll(Arrays.asList(handlers));
        return this;
    }

    public void start() throws IOException {
        synchronized (this) {
            if (isRunning) {
                return;
            }
            isRunning = true;
        }
        Thread thread = new Thread(() -> {
            while (isRunning) {
                String signal = "";
                try (Socket socket = serverSocket.accept()) {
                    socket.setSoTimeout(2000);
                    InputStream inputStream = socket.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(8123);
                    byte[] buffer = new byte[128];
                    int n;
                    try {
                        while ((n = inputStream.read(buffer)) != -1) {
                            baos.write(buffer, 0, n);
                        }
                    } catch (SocketTimeoutException ignored) {
                    }
                    signal = baos.toString(StandardCharsets.UTF_8);
                    baos.close();
                    socket.getOutputStream()
                            .write("HTTP/1.1 200 OK\r\nContent-Length: 0\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
                for (SignalHandler handler : handlers) {
                    if (handler.canHandle(signal)) {
                        handler.handle(signal);
                    }
                }
            }
        });
        thread.setName("SignalServer");
        thread.start();
    }

    public void stop() throws IOException {
        synchronized (this) {
            if (isRunning) {
                isRunning = false;
                serverSocket.close();
            }
        }
    }
}
