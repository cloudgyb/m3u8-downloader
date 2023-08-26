package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.SSLException;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class ProxyHttpsTunnelChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel channel) throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(new AllowAllCertX509TrustManager()) // 忽略目标服务器证书校验
                .build();
        channel.pipeline()
                .addLast("ssl", sslContext.newHandler(channel.alloc()))
                .addLast("alpn", new HttpApplicationProtocolNegotiationHandler(false));
    }

    private static class AllowAllCertX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}