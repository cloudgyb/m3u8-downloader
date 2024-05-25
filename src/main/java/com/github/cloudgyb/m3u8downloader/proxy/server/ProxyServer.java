package com.github.cloudgyb.m3u8downloader.proxy.server;

import com.github.cloudgyb.m3u8downloader.proxy.ProxyApplication;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
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

    public void start(ProxyApplication.StartedCallback callback) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            ChannelFuture future = serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("httpConnectRequestDecoder", new HttpRequestDecoder())
                                    .addLast("httpConnectResponseEncoder", new HttpResponseEncoder());
                            if (authenticationConfig.enableProxyAuth()) {
                                pipeline.addLast(new ProxyServerBasicAuthenticationHandler(authenticationConfig));
                            }
                            pipeline.addLast("proxyServer", new ProxyServerHandler())
                                    .addLast("exceptionHandler", ProxyExceptionHandler.getInstance())
                                    .addLast("idleHandler", new IdleHandler(60, 60, 120));
                        }
                    }).bind(host, port);
            future.addListener(future1 -> {
                if (future1.isSuccess()) {
                    logger.info("Proxy Server listen at {}:{}...", host, port);
                    callback.started(true);
                    ProxyApplication.onProxyServerStarted();
                } else {
                    logger.error(future1.cause().toString());
                    callback.started(false);
                    ProxyApplication.onProxyServerStartFailed();
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

    public void stop(ProxyApplication.StopCallback callback) {
        if (serverChannel != null) {
            serverChannel.close();
            worker.shutdownGracefully();
            boss.shutdownGracefully().addListener(future -> {
                callback.stopped(future.isSuccess());
            });
        } else {
            callback.stopped(true);
        }
    }
}
