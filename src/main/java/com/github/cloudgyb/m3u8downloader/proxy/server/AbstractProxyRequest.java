package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.Channel;

public abstract class AbstractProxyRequest<H, B> implements ProxyRequest<H, B> {
    private final Channel proxyServerChannel;
    private final Channel targetServerChannel;

    public AbstractProxyRequest(Channel proxyServerChannel, Channel targetServerChannel) {
        this.proxyServerChannel = proxyServerChannel;
        this.targetServerChannel = targetServerChannel;
    }

    @Override
    public Channel proxyServerChannel() {
        return proxyServerChannel;
    }

    @Override
    public Channel targetServerChannel() {
        return targetServerChannel;
    }
}
