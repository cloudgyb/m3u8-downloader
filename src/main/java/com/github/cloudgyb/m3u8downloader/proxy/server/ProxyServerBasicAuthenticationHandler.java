package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.Base64;

/**
 * 代理 basic 认证处理器
 *
 * @author cloudgyb
 */
@ChannelHandler.Sharable
public class ProxyServerBasicAuthenticationHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private final ProxyServerAuthenticationConfig authenticationConfig;

    public ProxyServerBasicAuthenticationHandler(ProxyServerAuthenticationConfig authenticationConfig) {
        this.authenticationConfig = authenticationConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest req) {
        HttpMethod method = req.method();
        if (method.equals(HttpMethod.CONNECT)) { // https 代理处理
            DefaultFullHttpResponse res = handleProxyAuthentication(req);
            if (res != null) {
                ctx.writeAndFlush(res);
                return;
            }
        }
        ctx.fireChannelRead(req);
    }

    private DefaultFullHttpResponse handleProxyAuthentication(HttpRequest req) {
        if (!authenticationConfig.enableProxyAuth()) {
            return null;
        }
        String authorInfo = req.headers().get(HttpHeaderNames.PROXY_AUTHORIZATION);
        DefaultFullHttpResponse resp = new DefaultFullHttpResponse(
                req.protocolVersion(),
                HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED);
        resp.headers().add(HttpHeaderNames.PROXY_AUTHENTICATE, "Basic realm=\"proxy\"");
        if (authorInfo == null || authorInfo.isBlank() || !authorInfo.startsWith("Basic ")) {
            return resp;
        }
        authorInfo = authorInfo.replaceFirst("Basic ", "");
        byte[] decode = Base64.getDecoder().decode(authorInfo);
        String usernameAndPassword = new String(decode);
        String[] split = usernameAndPassword.split(":");
        if (split.length != 2) {
            return resp;
        }
        String uname = split[0];
        String passwd = split[1];
        if (authenticationConfig.username().equals(uname) && authenticationConfig.password().equals(passwd)) {
            return null;
        }
        return resp;
    }
}
