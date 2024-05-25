package com.github.cloudgyb.m3u8downloader.proxy.server;


import io.netty.channel.Channel;

public interface ProxyRequest<H, B> {

    Channel proxyServerChannel();

    Channel targetServerChannel();

    void addHeader(HttpHeaders h);

    void addBodyData(B b);

    void proxy();
}
