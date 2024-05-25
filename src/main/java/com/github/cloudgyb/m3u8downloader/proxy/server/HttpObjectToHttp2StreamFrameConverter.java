package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.*;
import io.netty.handler.ssl.SslHandler;

public class HttpObjectToHttp2StreamFrameConverter {
    private static volatile HttpObjectToHttp2StreamFrameConverter instance;
    private final boolean validateHeaders = true;

    public static HttpObjectToHttp2StreamFrameConverter getInstance() {
        if (instance == null) {
            synchronized (HttpObjectToHttp2StreamFrameConverter.class) {
                if (instance == null) {
                    instance = new HttpObjectToHttp2StreamFrameConverter();
                }
            }
        }
        return instance;
    }

    public Http2StreamFrame convert(ChannelHandlerContext ctx, HttpObject obj) {
        // 1xx (excluding 101) is typically a FullHttpResponse, but the decoded
        // Http2HeadersFrame should not be marked as endStream=true
        if (obj instanceof HttpResponse res) {
            final HttpResponseStatus status = res.status();
            final int code = status.code();
            final HttpStatusClass statusClass = status.codeClass();
            // An informational response using a 1xx status code other than 101 is
            // transmitted as a HEADERS frame
            if (statusClass == HttpStatusClass.INFORMATIONAL && code != 101) {
                if (res instanceof FullHttpResponse) {
                    final Http2Headers headers = toHttp2Headers(ctx, res);
                    return (new DefaultHttp2HeadersFrame(headers, false));
                } else {
                    throw new EncoderException(status + " must be a FullHttpResponse");
                }
            }
        }

        if (obj instanceof HttpMessage) {
            Http2Headers headers = toHttp2Headers(ctx, (HttpMessage) obj);
            boolean noMoreFrames = false;
            if (obj instanceof FullHttpMessage full) {
                noMoreFrames = !full.content().isReadable() && full.trailingHeaders().isEmpty();
            }

            return (new DefaultHttp2HeadersFrame(headers, noMoreFrames));
        }

        if (obj instanceof LastHttpContent last) {
            return encodeLastContent(last);
        } else if (obj instanceof HttpContent cont) {
            return (new DefaultHttp2DataFrame(cont.content().retain(), false));
        }
        return null;
    }

    private Http2StreamFrame encodeLastContent(LastHttpContent last) {
        boolean needFiller = !(last instanceof FullHttpMessage) && last.trailingHeaders().isEmpty();
        if (last.content().isReadable() || needFiller) {
            return (new DefaultHttp2DataFrame(last.content().retain(), last.trailingHeaders().isEmpty()));
        }
        if (!last.trailingHeaders().isEmpty()) {
            Http2Headers headers = HttpConversionUtil.toHttp2Headers(last.trailingHeaders(), validateHeaders);
            return (new DefaultHttp2HeadersFrame(headers, true));
        }
        return null;
    }

    private Http2Headers toHttp2Headers(final ChannelHandlerContext ctx, final HttpMessage msg) {
        if (msg instanceof HttpRequest) {
            msg.headers().set(
                    HttpConversionUtil.ExtensionHeaderNames.SCHEME.text(),
                    connectionScheme(ctx));
        }

        return HttpConversionUtil.toHttp2Headers(msg, validateHeaders);
    }

    private static HttpScheme connectionScheme(ChannelHandlerContext ctx) {
        return isSsl(ctx) ? HttpScheme.HTTPS : HttpScheme.HTTP;
    }

    protected static boolean isSsl(final ChannelHandlerContext ctx) {
        final Channel connChannel = connectionChannel(ctx);
        return null != connChannel.pipeline().get(SslHandler.class);
    }

    private static Channel connectionChannel(ChannelHandlerContext ctx) {
        final Channel ch = ctx.channel();
        return ch instanceof Http2StreamChannel ? ch.parent() : ch;
    }

}
