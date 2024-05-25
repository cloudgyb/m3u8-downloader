package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Http CONNECT 请求解码出目标服务器的主机名和端口号
 */
public class HttpConnectMethodHostAndPortDecoder {
    public static HostAndPort decoder(HttpRequest request) {
        String uri = request.uri();
        String[] split = uri.split(":");
        String host = split[0];
        String port = split[1];
        int portInt = Integer.parseInt(port);
        return new HostAndPort(host, portInt);
    }

    public static class HostAndPort {
        private final String host;
        private final int port;

        public HostAndPort(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }
}
