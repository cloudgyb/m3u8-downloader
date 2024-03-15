package com.github.cloudgyb.m3u8downloader.proxy.server;


import com.github.cloudgyb.m3u8downloader.proxy.x509cert.X509CertGenerator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.*;
import com.github.cloudgyb.m3u8downloader.proxy.util.LRUCache;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ProxyServerHttpsChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private final SslContext sslContext;
    private static final LRUCache<String, String[]> certCache = LRUCache.getDefault(5);

    public ProxyServerHttpsChannelInitializer(String domain) throws IOException, OperatorCreationException {
        String[] strings = getX509CertAndPrivateKey(domain);
        ByteArrayInputStream privateKeyIs = new ByteArrayInputStream(strings[0].getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream certIs = new ByteArrayInputStream(strings[1].getBytes(StandardCharsets.UTF_8));
        SslContextBuilder sslContextBuilder = SslContextBuilder
                .forServer(certIs, privateKeyIs)
                .sslProvider(SslProvider.JDK)
                .clientAuth(ClientAuth.NONE);
        ApplicationProtocolConfig apn = new ApplicationProtocolConfig(
                ApplicationProtocolConfig.Protocol.ALPN,
                ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT,
                ApplicationProtocolNames.HTTP_1_1,
                ApplicationProtocolNames.HTTP_2
        );
        sslContextBuilder.applicationProtocolConfig(apn);
        sslContext = sslContextBuilder.build();
    }

    private static String[] getX509CertAndPrivateKey(String domain) throws IOException, OperatorCreationException {
        String[] strings = certCache.get(domain);
        if (strings == null) {
            strings = X509CertGenerator.generateProxyServerHostX509Cert(domain);
            certCache.put(domain, strings);
        }
        return strings;
    }

    @Override
    protected void initChannel(NioSocketChannel channel) {
        channel.pipeline()
                .remove("proxyServer");
        channel.pipeline()
                .addLast("ssl", sslContext.newHandler(channel.alloc()))
                .addLast("alpn", new HttpApplicationProtocolNegotiationHandler(true));
    }
}