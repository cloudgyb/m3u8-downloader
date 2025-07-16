package com.github.cloudgyb.m3u8downloader.signal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 浏览器插件通知处理器
 * 接受自动发现的 m3u8 链接，并创建下载任务
 *
 * @author cloudgyb
 * @since 2024/12/20 20:22
 */
public class HttpServerHandler implements SignalHandler {
    private static final Logger log = LoggerFactory.getLogger(HttpServerHandler.class);
    private final DownloadTaskService downloadTaskService = DownloadTaskService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean canHandle(String signal) {
        return signal.startsWith("POST / HTTP/1.1\r\n");
    }

    @Override
    public void handle(String signal) {
        String[] split = signal.split("\r\n\r\n");
        String body = split[1];
        if (body == null || body.isEmpty()) {
            log.error("接收到空 body 信号");
            return;
        }
        ObjectReader objectReader = objectMapper.readerFor(CrxNewDownloadTask.class);
        CrxNewDownloadTask crxNewDownloadTask;
        try {
            crxNewDownloadTask = objectReader.readValue(body);
        } catch (JsonProcessingException e) {
            log.error("浏览器插件通知新建任务 body 解析失败", e);
            return;
        }
        log.info("接收到浏览器插件新建任务信号:{}", crxNewDownloadTask);
        String url = crxNewDownloadTask.getUrl();
        String decodeUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
        DownloadTaskEntity entity = new DownloadTaskEntity();
        entity.setUrl(decodeUrl);
        entity.setCreateTime(new Date());
        entity.setDownloadDuration(0L);
        entity.setFinishMediaSegment(0);
        entity.setTotalMediaSegment(0);
        int maxThread = ApplicationStore.getSystemConfig().getDefaultThreadCount();
        entity.setMaxThreadCount(maxThread);
        entity.setSaveFilename(crxNewDownloadTask.getTitle().replace(" ",""));
        entity.setStage(DownloadTaskStageEnum.NEW.name());
        entity.setStatus(DownloadTaskStageEnum.NEW.name());
        downloadTaskService.save(entity);
        final DownloadTaskViewModel task = new DownloadTaskViewModel(entity);
        ApplicationStore.getNoFinishedTasks().add(task);
    }


    /**
     * 浏览器插件发送的信号 json 封装类
     *
     * @author cloudgyb
     * @since 2025/7/5 19:58
     */
    @SuppressWarnings("all")
    public static class CrxNewDownloadTask {
        public String title;
        public String url;
        private Long discoveryTime;

        public CrxNewDownloadTask() {
            this.title = "";
            this.url = "";
            this.discoveryTime = 0L;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public long getDiscoveryTime() {
            return discoveryTime;
        }

        @Override
        public String toString() {
            return "CrxNewDownloadTask{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", discoveryTime=" + discoveryTime +
                    '}';
        }
    }
}
