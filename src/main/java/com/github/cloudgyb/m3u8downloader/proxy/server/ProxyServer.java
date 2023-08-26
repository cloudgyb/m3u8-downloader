package com.github.cloudgyb.m3u8downloader.proxy.server;

import com.github.cloudgyb.m3u8downloader.proxy.ProxyApplication;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代理服务器实现
 *
 * @author cloudgyb
 */
public class ProxyServer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final NioEventLoopGroup boss = new NioEventLoopGroup();
    private final NioEventLoopGroup worker = new NioEventLoopGroup();
    private final String host;
    private final int port;
    private final ProxyServerAuthenticationConfig authenticationConfig;
    private volatile Channel serverChannel;

    public ProxyServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.authenticationConfig = new ProxyServerAuthenticationConfig(false, null, null);
    }

    public ProxyServer(String host, int port, boolean enableProxyAuth, String username, String password) {
        this.host = host;
        this.port = port;
        this.authenticationConfig = new ProxyServerAuthenticationConfig(enableProxyAuth, username, password);
    }

    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            ChannelFuture future = serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) {
                            channel.pipeline()
                                    .addLast("httpServerCodec", new HttpServerCodec())
                                    .addLast("HttpObjAgg", new HttpObjectAggregator(1024))
                                    .addLast("proxyServer", new ProxyServerHandler(authenticationConfig));
                        }
                    }).bind(host, port);
            future.addListener(future1 -> {
                if (future1.isSuccess()) {
                    logger.info("Proxy Server listen at {}:{}...", host, port);
                    ProxyApplication.onProxyServerStarted();
                }
            });
            serverChannel = future.sync().channel();
            serverChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("产生中断异常！");
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
            logger.info("Proxy Server {}:{} stopped!", host, port);
        }
    }

    public void stop() {
        if (serverChannel != null) {
            serverChannel.close();
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
