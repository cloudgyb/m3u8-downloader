package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.*;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;

import static com.github.cloudgyb.m3u8downloader.proxy.server.ProxyServerHandler.proxyServerHostAndPortAttrKey;

/**
 * 代理充当客户端与目标服务器建立连接 channel 初始化器 (https 协议通道配置)
 *
 * @author cloudgyb
 */
@ChannelHandler.Sharable
public class ProxyHttpsChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private final ApplicationProtocolConfig apn = new ApplicationProtocolConfig(
            ApplicationProtocolConfig.Protocol.ALPN,
            ApplicationProtocolConfig.SelectorFailureBehavior.FATAL_ALERT,
            ApplicationProtocolConfig.SelectedListenerFailureBehavior.FATAL_ALERT,
            ApplicationProtocolNames.HTTP_1_1,
            ApplicationProtocolNames.HTTP_2
    );

    @Override
    protected void initChannel(NioSocketChannel channel) throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient()
                .sslProvider(SslProvider.JDK)
                .applicationProtocolConfig(apn)
                .trustManager(InsecureTrustManagerFactory.INSTANCE) // 忽略目标服务器证书校验
                .build();
        //从 channel 的 attr 中获取服务器的主机名和端口号，
        // 用于创建 SSLHandler，SSLEngine 设置 TLS Server_name 扩展，以避免 TLS 握手失败
        HttpConnectMethodHostAndPortDecoder.HostAndPort hostAndPort = channel.attr(proxyServerHostAndPortAttrKey).get();
        String hostName = hostAndPort.getHost();
        int port = hostAndPort.getPort();
        channel.pipeline()
                .addLast("ssl", sslContext.newHandler(channel.alloc(), hostName, port))
                .addLast("alpn", new HttpApplicationProtocolNegotiationHandler(false))
                .addLast("exceptionHandler", ProxyExceptionHandler.getInstance())
                .addLast("idleHandler", new IdleHandler(60, 60, 120));
    }
}