package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代理请求处理器
 *
 * @author cloudgyb
 */
public class ProxyServerHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    final static AttributeKey<Channel> targetServerAttrKey = AttributeKey.newInstance("targetServerChannel");
    final static AttributeKey<Channel> proxyServerAttrKey = AttributeKey.newInstance("proxyServerChannel");
    final static AttributeKey<HttpConnectMethodHostAndPortDecoder.HostAndPort> proxyServerHostAndPortAttrKey =
            AttributeKey.newInstance("proxyServerHostAndPort");
    private final static NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private final Bootstrap httpBootstrap = new Bootstrap();
    private final Bootstrap httpsBootstrap = new Bootstrap();
    private static final ProxyHttpChannelInitializer proxyHttpChannelInitializer = new ProxyHttpChannelInitializer();
    private static final ProxyHttpsChannelInitializer proxyHttpsChannelInitializer = new ProxyHttpsChannelInitializer();

    public ProxyServerHandler() {
        this.httpBootstrap
                .channel(NioSocketChannel.class)
                .group(eventLoopGroup)
                .handler(proxyHttpChannelInitializer);
        this.httpsBootstrap
                .channel(NioSocketChannel.class)
                .group(eventLoopGroup)
                .handler(proxyHttpsChannelInitializer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest req) {
            HttpMethod method = req.method();
            if (method.equals(HttpMethod.CONNECT)) { // https 代理处理
                HttpConnectMethodHostAndPortDecoder.HostAndPort hostAndPort =
                        HttpConnectMethodHostAndPortDecoder.decoder(req);
                String host = hostAndPort.getHost();
                Channel proxyServerChannel = ctx.channel();
                // 连接到目标服务器
                Channel targetServerChannel = connectToTargetServer(hostAndPort, proxyServerChannel, true,
                        req.protocolVersion());
                // 目标服务器 channel 与 代理服务器 channel 互相绑定，用于互相转发数据
                proxyServerChannel.attr(targetServerAttrKey).set(targetServerChannel);
                targetServerChannel.attr(proxyServerAttrKey).set(proxyServerChannel);
                try {
                    // 初始化
                    new ProxyServerHttpsChannelInitializer(host)
                            .initChannel((NioSocketChannel) proxyServerChannel);
                    // 响应 200 Connection established，表示隧道建立成功
                    DefaultFullHttpResponse resp = new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.OK);
                    resp.setStatus(new HttpResponseStatus(200, "Connection established"));
                    ctx.writeAndFlush(resp);
                } catch (Exception e) {
                    DefaultFullHttpResponse resp = new DefaultFullHttpResponse(req.protocolVersion(),
                            HttpResponseStatus.INTERNAL_SERVER_ERROR);
                    ctx.writeAndFlush(resp);
                    logger.error("代理服务器处理异常！", e);
                    ctx.close();
                }
            } else { // http 代理处理
                String hostHeader = req.headers().get("host");
                String host;
                String port;
                if (hostHeader.contains(":")) {
                    String[] split = hostHeader.split(":");
                    host = split[0];
                    port = split[1];
                } else {
                    host = hostHeader;
                    port = "80";
                }
                Channel targetServerChannel = connectToTargetServer(
                        new HttpConnectMethodHostAndPortDecoder.HostAndPort(host, Integer.parseInt(port)),
                        ctx.channel(), false, req.protocolVersion());
                Channel proxyServerChannel = ctx.channel();
                // 目标服务器 channel 与 代理服务器 channel 互相绑定，用于互相转发数据
                proxyServerChannel.attr(targetServerAttrKey).set(targetServerChannel);
                targetServerChannel.attr(proxyServerAttrKey).set(proxyServerChannel);
                ProxyServerHttpChannelInitializer.getInstance().initChannel((NioSocketChannel) proxyServerChannel);
                targetServerChannel.writeAndFlush(req);
            }
            ctx.pipeline().remove("httpConnectRequestDecoder");
            ctx.pipeline().remove("httpConnectResponseEncoder");
        } else {
            Channel targetServerChannel = ctx.channel().attr(targetServerAttrKey).get();
            targetServerChannel.writeAndFlush(msg);
        }
    }

    /**
     * 连接到目标服务器
     *
     * @param hostAndPort 主机名或者 ip 地址和端口号
     * @return 连接建立就绪的 channel
     */
    private Channel connectToTargetServer(HttpConnectMethodHostAndPortDecoder.HostAndPort hostAndPort,
                                          Channel proxyServerChannel, boolean isHttps, HttpVersion httpVersion) {
        Bootstrap bootstrap = isHttps ? httpsBootstrap : httpBootstrap;
        String host = hostAndPort.getHost();
        int port = hostAndPort.getPort();
        ChannelFuture future = bootstrap.connect(host, port);
        // 将主机名和端口信息绑定到代理服务器连接 channel，
        // 用于创建 SSLHandler ，SSLEngine 设置 TLS Server_name 扩展，以避免 TLS 握手失败
        future.channel().attr(proxyServerHostAndPortAttrKey).set(hostAndPort);

        future.addListener(future1 -> {
            if (future1.isSuccess()) {
                logger.info("目标服务器{}：{}连接已建立！", host, port);
            } else {
                DefaultFullHttpResponse resp502 = new DefaultFullHttpResponse(httpVersion,
                        HttpResponseStatus.BAD_GATEWAY);
                proxyServerChannel.writeAndFlush(resp502)
                        .addListener(future2 -> proxyServerChannel.close());
                logger.error("目标服务器%s:%d连接建立失败！".formatted(host, port), future1.cause());
            }
        });

        try {
            return future.sync().channel();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}