package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

import static com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServerHandler.targetServerAttrKey;
import static com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServerHandler.proxyServerAttrKey;

/**
 * 代理服务器异常处理器
 *
 * @author cloudgyb
 */
@ChannelHandler.Sharable
public class ProxyExceptionHandler extends ChannelDuplexHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static volatile ProxyExceptionHandler instance;

    private ProxyExceptionHandler() {
    }

    public static ProxyExceptionHandler getInstance() {
        if (instance == null) {
            synchronized (ProxyExceptionHandler.class) {
                if (instance == null) {
                    instance = new ProxyExceptionHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        // 处理代理服务器 channel 关闭，主动关闭目标服务器 channel
        Channel channel = ctx.channel().attr(targetServerAttrKey).get();
        if (channel != null) {
            SocketAddress socketAddress = channel.remoteAddress();
            logger.info("[代理服务器]连接关闭，关闭[目标服务器]{}连接！", socketAddress);
            Channel tergetServerChannel = channel;
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(future -> tergetServerChannel.close());
        }
        // 处理目标服务器 channel 关闭，主动关闭代理服务器 channel
        channel = ctx.channel().attr(proxyServerAttrKey).get();
        if (channel != null) {
            SocketAddress socketAddress = channel.remoteAddress();
            logger.info("[目标服务器]连接关闭，关闭[代理服务器]{}连接！", socketAddress);
            Channel proxyServerChannel = channel;
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(future -> proxyServerChannel.close());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 处理代理服务器 channel 异常关闭，主动关闭目标服务器 channel
        Channel channel = ctx.channel().attr(targetServerAttrKey).get();
        if (channel != null) {
            SocketAddress socketAddress = channel.remoteAddress();
            logger.info("[代理服务器]连接[异常]关闭，关闭[目标服务器]{}连接！{}", socketAddress, cause.getMessage());
            Channel tergetServerChannel = channel;
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(future -> tergetServerChannel.close());
        }
        // 处理目标服务器 channel 异常关闭，主动关闭代理服务器 channel
        channel = ctx.channel().attr(proxyServerAttrKey).get();
        if (channel != null) {
            SocketAddress socketAddress = channel.remoteAddress();
            logger.info("[目标服务器]连接[异常]关闭，关闭[代理服务器]{}连接！{}", socketAddress, cause.getMessage());
            Channel proxyServerChannel = channel;
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(future -> proxyServerChannel.close());
        }
    }
}
