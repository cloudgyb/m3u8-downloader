package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServerHandler.proxyServerAttrKey;

/**
 * 目标服务器响应处理器
 * 将目标服务器的响应转到代理服务器的通道
 *
 * @author cloudgyb
 */
@ChannelHandler.Sharable
public class TargetServerResponseHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpResponse response) {
            HttpResponseStatus status = response.status();
            HttpVersion httpVersion = response.protocolVersion();
            logger.debug(ctx.channel() + " 响应：" + status.toString() + " " + httpVersion.toString());
        }
        ctx.channel().attr(proxyServerAttrKey).get().writeAndFlush(msg);
    }
}
