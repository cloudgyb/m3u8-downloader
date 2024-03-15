package com.github.cloudgyb.m3u8downloader.proxy.server;

import com.github.cloudgyb.m3u8downloader.viewcontroller.CaptureM3u8ViewController;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
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
        }
        ctx.channel().attr(targetServerAttrKey).get().writeAndFlush(msg);
    }
}
