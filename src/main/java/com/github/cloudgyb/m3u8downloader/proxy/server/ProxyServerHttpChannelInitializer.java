package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;

/**
 * 代理服务器 http channel 初始化
 */
@ChannelHandler.Sharable
public class ProxyServerHttpChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private static volatile ProxyServerHttpChannelInitializer instance;
    private ProxyServerHttpChannelInitializer() {
    }

    @Override
    protected void initChannel(NioSocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.remove("proxyServer");
        pipeline.addLast("httpRequestDecoder", new HttpRequestDecoder())
                .addLast("proxyServerRequestHandler", ProxyServerRequestHandler.getInstance());
    }

    public static ProxyServerHttpChannelInitializer getInstance() {
        if(instance == null) {
            synchronized (ProxyServerHttpChannelInitializer.class) {
                if(instance == null) {
                    instance = new ProxyServerHttpChannelInitializer();
                }
            }
        }
        return instance;
    }
}
