package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProtocols;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;

/**
 * 代理充当客户端与目标服务器建立连接 channel 初始化器 (https 协议通道配置)
 *
 * @author cloudgyb
 */
@ChannelHandler.Sharable
public class ProxyHttpsChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel channel) throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient()
                .sslProvider(SslProvider.JDK)
                .trustManager(InsecureTrustManagerFactory.INSTANCE) // 忽略目标服务器证书校验
                .build();
        channel.pipeline()
                .addLast("ssl", sslContext.newHandler(channel.alloc()))
                .addLast("alpn", new HttpApplicationProtocolNegotiationHandler(false))
                .addLast("exceptionHandler", ProxyExceptionHandler.getInstance())
                .addLast("idleHandler", new IdleHandler(10, 10, 20));
    }
}