package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Frame;
import io.netty.handler.ssl.ApplicationProtocolNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.cloudgyb.m3u8downloader.proxy.server.HttpApplicationProtocolNegotiationHandler.httpVersionAttrKey;
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
        } else if (msg instanceof Http2Frame frame) {
            Channel proxyServerChannel = ctx.channel().attr(proxyServerAttrKey).get();
            String httpVersion = proxyServerChannel.attr(httpVersionAttrKey).get();
            if (ApplicationProtocolNames.HTTP_1_1.equals(httpVersion)) {
                try {
                    msg = Http2StreamFrameToHttpObjectConverter.forClient()
                            .convert(frame, ctx);
                } catch (Http2Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (msg != null) {
            ctx.channel().attr(proxyServerAttrKey).get().writeAndFlush(msg);
        }
    }
}
