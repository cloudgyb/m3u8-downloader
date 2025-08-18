package com.github.cloudgyb.m3u8downloader.util;

import com.github.cloudgyb.m3u8downloader.conf.ProxyConfig;
import com.github.cloudgyb.m3u8downloader.conf.SocksProxySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

/**
 * Http客户端工具类
 *
 * @author cloudgyb
 * 2021/5/17 16:01
 */
public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final HttpClient.Builder httpClientBuilder = HttpClient.newBuilder();
    private volatile static HttpClient httpClient;
    private static final ProxyConfig proxyConfig;

    static {
        proxyConfig = new ProxyConfig();
        httpClient = httpClientBuilder
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public static void proxyAuthUpdate(ProxyConfig proxyConfig) {
        ProxyConfig proxyConfig1 = HttpClientUtil.proxyConfig;
        proxyConfig1.setProxyUsername(proxyConfig.getProxyUsername());
        proxyConfig1.setProxyPassword(proxyConfig.getProxyPassword());
    }

    public static void proxyConfigUpdate(ProxyConfig proxyConfig) {
        boolean oldProxyConfigProxyEnabled = HttpClientUtil.proxyConfig.isProxyEnabled();
        HttpClientUtil.proxyConfig.update(proxyConfig);
        // 如果代理未开启且本次更新配置也未开启代理，则不更新HttpClient
        if (!oldProxyConfigProxyEnabled && !proxyConfig.isProxyEnabled()) {
            return;
        }
        if (proxyConfig.isProxyEnabled()) {
            ProxySelector proxySelector;
            log.info("启用代理：{}", proxyConfig);
            if (Proxy.Type.SOCKS.equals(proxyConfig.getProxyType())) {
                proxySelector = new SocksProxySelector(proxyConfig.getProxyHost(), proxyConfig.getProxyPort());
            } else {
                InetSocketAddress socketAddr = new InetSocketAddress(
                        proxyConfig.getProxyHost(),
                        proxyConfig.getProxyPort());
                proxySelector = ProxySelector.of(socketAddr);
            }
            httpClient = httpClientBuilder
                    .proxy(proxySelector)
                    .build();
        } else {
            log.info("关闭代理");
            httpClient = httpClientBuilder
                    .proxy(ProxySelector.of(null))
                    .build();
        }
    }

    @SuppressWarnings("unused")
    public static byte[] getAsByte(String url) throws IOException, InterruptedException {
        return execGet(url, HttpResponse.BodyHandlers.ofByteArray());
    }

    public static InputStream getAsInputStream(String url) {
        try {
            return execGet(url, HttpResponse.BodyHandlers.ofInputStream());
        } catch (Exception e) {
            log.error("执行请求失败！{}-{}:{}", url, e.getClass().getName(), e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); // 恢复中断,让后续逻辑能够获取中断状态
            }
            return ByteArrayInputStream.nullInputStream();
        }
    }

    @SuppressWarnings("unused")
    public static String getAsString(String url) throws IOException, InterruptedException {
        return execGet(url, HttpResponse.BodyHandlers.ofString());
    }

    private static <T> T execGet(String url, HttpResponse.BodyHandler<T> bodyHandler)
            throws IOException, InterruptedException {
        URI uri = URI.create(url);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
                .header("Referer", uri.toASCIIString())
                .timeout(Duration.ofSeconds(5));
        String basicCredentials;
        if (proxyConfig.isProxyEnabled()) {
            String proxyUsername = proxyConfig.getProxyUsername();
            String proxyPassword = proxyConfig.getProxyPassword();
            Base64.Encoder encoder = Base64.getEncoder();
            basicCredentials = encoder.encodeToString(
                    (proxyUsername + ":" + proxyPassword).getBytes(StandardCharsets.UTF_8)
            );
            builder.header("Proxy-Authorization", "Basic " + basicCredentials);
        }
        final HttpRequest request = builder.build();
        final HttpResponse<T> response = httpClient.send(request, bodyHandler);
        final int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new IOException("该视频无法下载！HTTP状态码：" + statusCode);
        }
        return response.body();
    }

}
