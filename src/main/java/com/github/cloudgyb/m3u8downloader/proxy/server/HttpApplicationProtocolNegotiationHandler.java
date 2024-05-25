package com.github.cloudgyb.m3u8downloader.proxy.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http2.Http2FrameCodecBuilder;
import io.netty.handler.codec.http2.Http2MultiplexHandler;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http 应用协议 SSL/TLS 协商结果处理器，根据协商的结果配置 channel pipeline。
 *
 * @author cloudgyb
 */
public class HttpApplicationProtocolNegotiationHandler extends ApplicationProtocolNegotiationHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static ProxyServerRequestHandler proxyRequestHandler = ProxyServerRequestHandler.getInstance();
    private final static TargetServerResponseHandler proxyResponseHandler = new TargetServerResponseHandler();
    final static AttributeKey<String> httpVersionAttrKey = AttributeKey.valueOf("httpVersion");
    private final boolean isServer;

    public HttpApplicationProtocolNegotiationHandler(boolean isServer) {
        // 如果协商失败使用 http/1.1 协议
        super(ApplicationProtocolNames.HTTP_1_1);
        this.isServer = isServer;
    }

    @Override
    protected void configurePipeline(ChannelHandlerContext ctx, String protocol) {
        logger.info(ctx + "协商的协议为" + protocol);
        ctx.channel().attr(httpVersionAttrKey).set(protocol);
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
            pipeline.addLast("proxyRequestHandler", proxyRequestHandler);
        } else {
            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast("proxyResponseHandler", proxyResponseHandler);
        }
    }

    private void configureHttp2(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        if (isServer) {
            pipeline.addLast(Http2FrameCodecBuilder.forServer().build())
                    .addLast("http2MultiplexHandler", new Http2MultiplexHandler(new SimpleChannelInboundHandler<>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
                        }
                    }))
                    .addLast("proxyRequestHandler", proxyRequestHandler);
        } else {
            pipeline.addLast(Http2FrameCodecBuilder.forClient().build())
                    .addLast(new Http2MultiplexHandler(new SimpleChannelInboundHandler<>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
                        }
                    }))
                    .addLast("proxyResponseHandler", proxyResponseHandler);
        }
    }

    @Override
    protected void handshakeFailure(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.handshakeFailure(ctx, cause);
    }
}
