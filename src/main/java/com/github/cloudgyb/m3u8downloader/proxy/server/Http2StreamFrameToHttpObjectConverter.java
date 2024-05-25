package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.*;

public class Http2StreamFrameToHttpObjectConverter {
    private static volatile Http2StreamFrameToHttpObjectConverter instanceForServer;
    private static volatile Http2StreamFrameToHttpObjectConverter instanceForClient;
    private final boolean isServer;
    private final boolean validateHeaders;

    public static Http2StreamFrameToHttpObjectConverter forServer() {
        if (instanceForServer == null) {
            synchronized (Http2StreamFrameToHttpObjectConverter.class) {
                if (instanceForServer == null) {
                    instanceForServer = new Http2StreamFrameToHttpObjectConverter(true);
                }
            }
        }
        return instanceForServer;
    }

    public static Http2StreamFrameToHttpObjectConverter forClient() {
        if (instanceForClient == null) {
            synchronized (Http2StreamFrameToHttpObjectConverter.class) {
                if (instanceForClient == null) {
                    instanceForClient = new Http2StreamFrameToHttpObjectConverter(false);
                }
            }
        }
        return instanceForClient;
    }


    private Http2StreamFrameToHttpObjectConverter(boolean isServer) {
        this.isServer = isServer;
        this.validateHeaders = true;
    }

    public HttpObject convert(Http2Frame frame, ChannelHandlerContext ctx) throws Http2Exception {
        if (frame instanceof Http2HeadersFrame headersFrame) {
            Http2Headers headers = headersFrame.headers();
            Http2FrameStream stream = headersFrame.stream();
            int id = stream == null ? 0 : stream.id();

            final CharSequence status = headers.status();

            // 1xx response (excluding 101) is a special case where Http2HeadersFrame#isEndStream=false
            // but we need to decode it as a FullHttpResponse to play nice with HttpObjectAggregator.
            if (null != status && isInformationalResponseHeaderFrame(status)) {
                return newFullMessage(id, headers, ctx.alloc());
            }

            if (headersFrame.isEndStream()) {
                if (headers.method() == null && status == null) {
                    LastHttpContent last = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, validateHeaders);
                    HttpConversionUtil.addHttp2ToHttpHeaders(id, headers, last.trailingHeaders(),
                            HttpVersion.HTTP_1_1, true, true);
                    return last;
                } else {
                    return newFullMessage(id, headers, ctx.alloc());
                }
            } else {
                HttpMessage msg = newMessage(id, headers);
                if (!HttpUtil.isContentLengthSet(msg)) {
                    msg.headers().add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                }
                return msg;
            }
        } else if (frame instanceof Http2DataFrame dataFrame) {
            if (dataFrame.isEndStream()) {
                return (new DefaultLastHttpContent(dataFrame.content().retain(), validateHeaders));
            } else {
                return (new DefaultHttpContent(dataFrame.content().retain()));
            }
        }
        return null;
    }

    private HttpMessage newMessage(final int id,
                                   final Http2Headers headers) throws Http2Exception {
        return isServer ?
                HttpConversionUtil.toHttpRequest(id, headers, validateHeaders) :
                HttpConversionUtil.toHttpResponse(id, headers, validateHeaders);
    }

    private FullHttpMessage newFullMessage(final int id,
                                           final Http2Headers headers,
                                           final ByteBufAllocator alloc) throws Http2Exception {
        return isServer ?
                HttpConversionUtil.toFullHttpRequest(id, headers, alloc, validateHeaders) :
                HttpConversionUtil.toFullHttpResponse(id, headers, alloc, validateHeaders);
    }

    private static boolean isInformationalResponseHeaderFrame(CharSequence status) {
        if (status.length() == 3) {
            char char0 = status.charAt(0);
            char char1 = status.charAt(1);
            char char2 = status.charAt(2);
            return char0 == '1'
                    && char1 >= '0' && char1 <= '9'
                    && char2 >= '0' && char2 <= '9' && char2 != '1';
        }
        return false;
    }
}
