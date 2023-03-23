package com.github.cloudgyb.m3u8downloader.m3u8;

import com.github.cloudgyb.m3u8downloader.util.HttpClientUtil;
import com.github.cloudgyb.m3u8downloader.util.URLUtil;
import io.lindstrom.m3u8.model.MasterPlaylist;
import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.Variant;
import io.lindstrom.m3u8.parser.MasterPlaylistParser;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;
import io.lindstrom.m3u8.parser.PlaylistParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.cloudgyb.m3u8downloader.util.URLUtil.addUrlSchemePrefixIfNeed;

/**
 * M3U8 索引文件解析工具类
 *
 * @author cloudgyb
 * @since 2.0.0
 */
public class M3U8Parser {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public List<com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment> playlistParse(String url) {
        List<String> masterPlaylist;
        List<com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment> mediaSegments = new ArrayList<>();
        try {
            logger.info("开始尝试解析{}为主播放列表", url);
            masterPlaylist = masterPlaylistUrlParse(url);
            logger.info("尝试解析{}为主播放列表完成，包含{}个播放列表！", url, masterPlaylist.size());
        } catch (PlaylistParserException e) {
            logger.error("尝试解析{}为主播放列表失败！{}", url, e.getClass());
            // 作为媒体播放列表进行处理
            masterPlaylist = Collections.singletonList(url);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("开始尝试解析为媒体播放列表...");
        for (String mpl : masterPlaylist) {
            try {
                logger.info("开始尝试解析{}为媒体播放列表...", mpl);
                List<com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment> mediaPlaylistUrls = mediaPlaylistParse(mpl);
                mediaSegments.addAll(mediaPlaylistUrls);
            } catch (PlaylistParserException e) {
                logger.error("尝试解析{}为媒体播放列表失败！{}", mpl, e.getClass());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("解析为媒体播放列表，一共{}个媒体片段！", mediaSegments.size());
        return mediaSegments;
    }

    @SuppressWarnings("unused")
    public List<String> playlistUrlParse(String url) {
        List<String> masterPlaylist;
        List<String> mediaPlaylist = new ArrayList<>();
        try {
            logger.info("开始尝试解析{}为主播放列表", url);
            masterPlaylist = masterPlaylistUrlParse(url);
            logger.info("尝试解析{}为主播放列表完成，包含{}个播放列表！", url, masterPlaylist.size());
        } catch (PlaylistParserException e) {
            logger.error("尝试解析{}为主播放列表失败！{}", url, e.getClass());
            // 作为媒体播放列表进行处理
            masterPlaylist = Collections.singletonList(url);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("开始尝试解析为媒体播放列表...");
        for (String mpl : masterPlaylist) {
            try {
                logger.info("开始尝试解析{}为媒体播放列表...", mpl);
                List<String> mediaPlaylistUrls = mediaPlaylistUrlParse(mpl);
                mediaPlaylist.addAll(mediaPlaylistUrls);
            } catch (PlaylistParserException e) {
                logger.error("尝试解析{}为媒体播放列表失败！{}", mpl, e.getClass());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("解析为媒体播放列表，一共{}个媒体片段！", mediaPlaylist.size());
        return mediaPlaylist;
    }

    public List<String> masterPlaylistUrlParse(String url) throws IOException, InterruptedException {
        String baseUrl = URLUtil.getBaseUrl(url);
        MasterPlaylistParser parser = new MasterPlaylistParser();
        InputStream inputStream = HttpClientUtil.getAsInputStream(url);
        List<String> masterPlaylistUrls = null;
        // Parse playlist
        MasterPlaylist playlist = parser.readPlaylist(inputStream);
        List<Variant> variants = playlist.variants();
        if (variants != null) {
            masterPlaylistUrls = variants.stream()
                    .map(Variant::uri)
                    .map(u -> addUrlSchemePrefixIfNeed(baseUrl, u))
                    .collect(Collectors.toList());
        }
        return masterPlaylistUrls;
    }

    public List<String> mediaPlaylistUrlParse(String url) throws IOException, InterruptedException {
        String baseUrl = URLUtil.getBaseUrl(url);
        MediaPlaylistParser parser = new MediaPlaylistParser();
        InputStream inputStream = HttpClientUtil.getAsInputStream(url);
        List<String> mediaPlaylistUrls = null;
        // Parse playlist
        MediaPlaylist playlist = parser.readPlaylist(inputStream);
        String s = parser.writePlaylistAsString(playlist);
        System.out.println(s);
        List<MediaSegment> mediaSegments = playlist.mediaSegments();
        if (mediaSegments != null) {
            mediaPlaylistUrls = mediaSegments.stream().map(MediaSegment::uri)
                    .map(u -> addUrlSchemePrefixIfNeed(baseUrl, u)).collect(Collectors.toList());
        }
        return mediaPlaylistUrls;
    }

    public List<com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment> mediaPlaylistParse(String url)
            throws IOException, InterruptedException {
        String baseUrl = URLUtil.getBaseUrl(url);
        MediaPlaylistParser parser = new MediaPlaylistParser();
        InputStream inputStream = HttpClientUtil.getAsInputStream(url);
        // Parse playlist
        MediaPlaylist playlist = parser.readPlaylist(inputStream);
        List<MediaSegment> rawMediaSegments = playlist.mediaSegments();
        ArrayList<com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment> mediaSegments = new ArrayList<>();
        if (rawMediaSegments != null) {
            rawMediaSegments.forEach(ms -> {
                String uri = ms.uri();
                double duration = ms.duration();
                String fullUrl = addUrlSchemePrefixIfNeed(baseUrl, uri);
                com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment mediaSegment = new
                        com.github.cloudgyb.m3u8downloader.m3u8.MediaSegment(fullUrl, duration);
                mediaSegments.add(mediaSegment);
            });
        }
        return mediaSegments;
    }
}
