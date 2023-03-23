package com.github.cloudgyb.m3u8downloader.util;

import java.net.MalformedURLException;
import java.net.URL;

public final class URLUtil {

    public static String addUrlSchemePrefixIfNeed(String baseUrl, String uri) {
        if (uri.startsWith("http://") || uri.startsWith("https://"))
            return uri;
        return baseUrl + uri;
    }

    public static String getBaseUrl(String url) throws MalformedURLException {
        URL url1 = new URL(url);
        String path = url1.getPath();
        int i = path.lastIndexOf('/');
        if (i != -1) {
            path = path.substring(0, i + 1);
        } else {
            path = "/";
        }
        return url1.getProtocol() + "://" +
                url1.getHost() + ":" +
                (url1.getPort() == -1 ? url1.getDefaultPort() : url1.getPort()) +
                path;
    }

    public static void main(String[] args) throws MalformedURLException {
        String baseUrl = getBaseUrl("http://lcoalhost");
        System.out.println(baseUrl);
        String baseUrl1 = getBaseUrl("http://lcoalhost/");
        System.out.println(baseUrl1);
        String baseUrl2 = getBaseUrl("http://lcoalhost/fsdhfklsdf/fsdhfksdjf.html");
        System.out.println(baseUrl2);
        String baseUrl3 = getBaseUrl("http://lcoalhost/fsdhfklsdf/fsdhfksdjf.m3u8");
        System.out.println(baseUrl3);
        String baseUrl4 = getBaseUrl("https://www.baidu.com/test/hls/2000/index.m3u8");
        System.out.println(baseUrl4);
    }
}
