package com.github.cloudgyb.m3u8downloader.signal;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
        new Thread(() -> {
            while (isRunning) {
                String signal;
                try (Socket socket = serverSocket.accept()) {
                    socket.setSoTimeout(5000);
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = inputStream.readAllBytes();
                    signal = new String(bytes, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (SignalHandler handler : handlers) {
                    if (handler.canHandle(signal)) {
                        handler.handle(signal);
                    }
                }
            }
        }).start();
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
