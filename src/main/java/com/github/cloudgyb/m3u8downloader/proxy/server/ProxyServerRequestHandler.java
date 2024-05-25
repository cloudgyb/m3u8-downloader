package com.github.cloudgyb.m3u8downloader.proxy.server;

import com.github.cloudgyb.m3u8downloader.viewcontroller.CaptureM3u8ViewController;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.Http2HeadersFrame;
import io.netty.handler.codec.http2.Http2StreamChannel;
import io.netty.handler.codec.http2.Http2StreamChannelBootstrap;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

import static com.github.cloudgyb.m3u8downloader.proxy.server.HttpApplicationProtocolNegotiationHandler.httpVersionAttrKey;
import static com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServerHandler.targetServerAttrKey;

/**
 * 代理请求处理器
 * 将请求转发到目标服务器
 *
 * @author cloudgyb
 */
@ChannelHandler.Sharable
public class ProxyServerRequestHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static volatile ProxyServerRequestHandler instance;
    private final static AttributeKey<Http2StreamChannelBootstrap> Http2StreamChannelBootstrapAttrKey =
            AttributeKey.valueOf("Http2StreamChannelBootstrap");
    private final static AttributeKey<ProxyRequest<HttpRequest, HttpContent>> proxyHttp1xRequestAttrKey =
            AttributeKey.valueOf("proxyRequestAttrKey");

    private ProxyServerRequestHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Channel proxyServerChannel = ctx.channel();
        Channel targetServerChannel = proxyServerChannel.attr(targetServerAttrKey).get();
        if (msg instanceof HttpRequest request) {
            HttpMethod method = request.method();
            String uri = request.uri();
            String host = request.headers().getAsString(HttpHeaderNames.HOST);
            HttpVersion httpVersion = request.protocolVersion();
            logger.debug(ctx.channel() + " 请求：" + method.name() + " " + host + uri + " " + httpVersion.toString());
            ChannelHandler sslHandler = ctx.channel().pipeline().get("ssl");
            String scheme = "http://";
            if (sslHandler != null) {
                scheme = "https://";
            }
            String url = scheme + host + uri;
            CaptureM3u8ViewController.addUri(host, url);
            request.headers().remove(HttpHeaderNames.PROXY_AUTHORIZATION);
            ProxyRequest<HttpRequest, HttpContent> http1xProxyRequest = new Http1xProxyRequest(proxyServerChannel, targetServerChannel,
                    request);
            ctx.channel().attr(proxyHttp1xRequestAttrKey).set(http1xProxyRequest);
        } else if (msg instanceof HttpContent httpContent) {
            ProxyRequest<HttpRequest, HttpContent> proxyRequest = ctx.channel().attr(proxyHttp1xRequestAttrKey).get();
            proxyRequest.addBodyData(httpContent);
            /*Channel channel = ctx.channel();
            String proxyServerHttpVersion = channel.attr(httpVersionAttrKey).get();
            String targetServerHttpVersion = targetServerChannel.attr(httpVersionAttrKey).get();
            if (ApplicationProtocolNames.HTTP_1_1.equals(proxyServerHttpVersion) &&
                    ApplicationProtocolNames.HTTP_2.equals(targetServerHttpVersion)) {
                msg = HttpObjectToHttp2StreamFrameConverter.getInstance().convert(ctx, httpObject);
                Attribute<Http2StreamChannelBootstrap> attr = targetServerChannel.attr(Http2StreamChannelBootstrapAttrKey);
                if (attr == null) {
                    synchronized (targetServerChannel) {
                        attr = targetServerChannel.attr(Http2StreamChannelBootstrapAttrKey);
                        if (attr == null) {
                            Http2StreamChannelBootstrap http2StreamChannelBootstrap = new Http2StreamChannelBootstrap(ctx.channel());
                            targetServerChannel.attr(Http2StreamChannelBootstrapAttrKey).set(http2StreamChannelBootstrap);
                        }
                    }
                }
                Http2StreamChannelBootstrap http2StreamChannelBootstrap = attr.get();
                Http2StreamChannel http2StreamChannel;
                try {
                    http2StreamChannel = http2StreamChannelBootstrap.open().syncUninterruptibly().get();
                    int id = http2StreamChannel.stream().id();
                    http2StreamChannel.pipeline().addLast("")
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                if (msg != null) {
                    http2StreamChannel.writeAndFlush(msg);
                }
                return;
            }*/
        } else if (msg instanceof Http2HeadersFrame http2HeadersFrame) {

        }
    }

    public static ProxyServerRequestHandler getInstance() {
        if (instance == null) {
            synchronized (ProxyServerRequestHandler.class) {
                if (instance == null) {
                    instance = new ProxyServerRequestHandler();
                }
            }
        }
        return instance;
    }
}
