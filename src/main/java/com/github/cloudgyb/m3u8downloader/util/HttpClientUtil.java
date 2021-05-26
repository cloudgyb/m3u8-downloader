package com.github.cloudgyb.m3u8downloader.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Http客户端工具类
 *
 * @author cloudgyb
 * 2021/5/17 16:01
 */
public class HttpClientUtil {

    public static byte[] getAsByte(String url) throws IOException, InterruptedException {
        return execGet(url, HttpResponse.BodyHandlers.ofByteArray());
    }

    public static InputStream getAsInputStream(String url) throws IOException, InterruptedException {
        return execGet(url, HttpResponse.BodyHandlers.ofInputStream());
    }

    public static String getAsString(String url) throws IOException, InterruptedException {
        return execGet(url, HttpResponse.BodyHandlers.ofString());
    }

    private static <T> T execGet(String url, HttpResponse.BodyHandler<T> bodyHandler) throws IOException, InterruptedException {
        final HttpClient httpClient = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
                .header("Referer", url)
                .timeout(Duration.ofSeconds(5))
                .build();
        final HttpResponse<T> response = httpClient.send(request, bodyHandler);
        final int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new IOException("该视频无法下载！HTTP状态码：" + statusCode);
        }
        return response.body();
    }

}
