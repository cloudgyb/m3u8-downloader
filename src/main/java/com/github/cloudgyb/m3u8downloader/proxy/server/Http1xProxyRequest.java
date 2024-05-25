package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.util.internal.RecyclableArrayList;

public class Http1xProxyRequest extends AbstractProxyRequest<HttpRequest, HttpContent>
        implements ProxyRequest<HttpRequest, HttpContent> {
    private final HttpRequest httpRequest;
    private final RecyclableArrayList httpBody;
    private HttpPostRequestDecoder postRequestDecoder;


    public Http1xProxyRequest(Channel proxyServerChannel, Channel targetServerChannel, HttpRequest request) {
        super(proxyServerChannel, targetServerChannel);
        this.httpRequest = request;
        this.httpBody = RecyclableArrayList.newInstance();
        if (HttpMethod.POST.equals(httpRequest.method())) {
            postRequestDecoder = new HttpPostRequestDecoder(request);
        }
    }


    @Override
    public void addHeader(HttpHeaders headers) {
        headers.forEach(h -> httpRequest.headers().add(h.getKey(), h.getValue()));
    }

    @Override
    public void addBodyData(HttpContent httpContent) {
        if (postRequestDecoder != null) {
            postRequestDecoder.offer(httpContent);
        } else {
            httpBody.add(httpContent);
        }
        if (httpContent instanceof LastHttpContent) {
            proxy();
        }
    }

    @Override
    public void proxy() {

    }
}
