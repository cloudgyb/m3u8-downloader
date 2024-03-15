package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;

public class ProxyHttpChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel channel) {
        channel.pipeline()
                .addLast(new HttpRequestEncoder() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        super.write(ctx, msg, promise);
                        ctx.pipeline().remove(this.getClass());
                    }
                })
                .addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                        Channel proxyServerChannel = ctx.channel().attr(ProxyServerHandler.proxyServerAttrKey).get();
                        proxyServerChannel.writeAndFlush(msg);
                    }
                })
                .addLast(new IdleHandler(10, 10, 20));
    }
}