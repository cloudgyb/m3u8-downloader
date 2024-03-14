package com.github.cloudgyb.m3u8downloader.proxy.server;


import com.github.cloudgyb.m3u8downloader.viewcontroller.CaptureM3u8ViewController;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.Http2FrameCodecBuilder;
import io.netty.handler.codec.http2.Http2StreamFrameToHttpObjectCodec;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServerHandler.proxyServerAttrKey;
import static com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServerHandler.targetServerAttrKey;

/**
 * Http 应用协议 SSL/TLS 协商结果处理器，根据协商的结果配置 channel pipeline。
 *
 * @author cloudgyb
 */
public class HttpApplicationProtocolNegotiationHandler extends ApplicationProtocolNegotiationHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final boolean isServer;

    public HttpApplicationProtocolNegotiationHandler(boolean isServer) {
        // 如果协商失败使用 http/1.1 协议
        super(ApplicationProtocolNames.HTTP_1_1);
        this.isServer = isServer;
    }

    @Override
    protected void configurePipeline(ChannelHandlerContext ctx, String protocol) {
        logger.info(ctx + "协商的协议为" + protocol);
        if (ApplicationProtocolNames.HTTP_2.equals(protocol)) {
            configureHttp2(ctx);
        } else if (ApplicationProtocolNames.HTTP_1_1.equals(protocol)) {
            configureHttp1(ctx);
        } else {
            throw new IllegalStateException("unknown protocol: " + protocol);
        }
    }

    private void configureHttp1(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        if (isServer) {
            pipeline.addLast(new HttpServerCodec());
        } else {
            pipeline.addLast(new HttpClientCodec());
        }
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 1024))
                .addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
                        ChannelHandler sslHandler = ctx.channel().pipeline().get("ssl");
                        String scheme = "http://";
                        if (sslHandler != null) {
                            scheme = "https://";
                        }
                        String uri = msg.uri();
                        String host = msg.headers().getAsString(HttpHeaderNames.HOST);
                        String url = scheme + host + uri;
                        CaptureM3u8ViewController.addUri(host, url);
                        msg.retain();
                        ctx.channel().attr(targetServerAttrKey).get().writeAndFlush(msg);
                    }
                }).addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {
                        msg.retain();
                        ctx.channel().attr(proxyServerAttrKey).get().writeAndFlush(msg);
                    }
                });
    }

    private void configureHttp2(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        if (isServer) {
            pipeline.addLast(Http2FrameCodecBuilder.forServer().build());
        } else {
            pipeline.addLast(Http2FrameCodecBuilder.forClient().build());
        }
        pipeline.addLast(new Http2StreamFrameToHttpObjectCodec(isServer)); //转换成 http/1.1 Object
        configureHttp1(ctx);
    }
}
