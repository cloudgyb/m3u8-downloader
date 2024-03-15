package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

import static com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServerHandler.targetServerAttrKey;
import static com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServerHandler.proxyServerAttrKey;

public class IdleHandler extends IdleStateHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public IdleHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        super.channelIdle(ctx, evt);
        IdleState state = evt.state();
        // 处理代理服务器 channel 关闭，主动关闭目标服务器 channel
        Channel channel = ctx.channel().attr(targetServerAttrKey).get();
        if (channel != null) {
            SocketAddress socketAddress = channel.remoteAddress();
            logger.info("[代理服务器]连接 IO 超时{}，关闭[目标服务器]{}连接！", socketAddress, state.name());
            Channel tergetServerChannel = channel;
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(future -> tergetServerChannel.close());
        }
        // 处理目标服务器 channel 关闭，主动关闭代理服务器 channel
        channel = ctx.channel().attr(proxyServerAttrKey).get();
        if (channel != null) {
            SocketAddress socketAddress = channel.remoteAddress();
            logger.info("[目标服务器]连接 IO 超时{}，关闭[代理服务器]{}连接！", socketAddress, state.name());
            Channel proxyServerChannel = channel;
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(future -> proxyServerChannel.close());
        }
        ctx.channel().writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(future -> ctx.close());
    }
}
