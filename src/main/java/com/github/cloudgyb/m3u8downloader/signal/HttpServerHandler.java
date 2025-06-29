package com.github.cloudgyb.m3u8downloader.signal;

import com.github.cloudgyb.m3u8downloader.ApplicationStore;
import com.github.cloudgyb.m3u8downloader.domain.DownloadTaskStageEnum;
import com.github.cloudgyb.m3u8downloader.domain.entity.DownloadTaskEntity;
import com.github.cloudgyb.m3u8downloader.domain.service.DownloadTaskService;
import com.github.cloudgyb.m3u8downloader.model.DownloadTaskViewModel;
import com.github.cloudgyb.m3u8downloader.util.SpringBeanUtil;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author cloudgyb
 * @since 2024/12/20 20:22
 */
public class HttpServerHandler implements SignalHandler {
    private volatile DownloadTaskService downloadTaskService;


    @Override
    public boolean canHandle(String signal) {
        return signal.startsWith("POST / HTTP/1.1\r\n");
    }

    @Override
    public void handle(String signal) {
        if (downloadTaskService == null) {
            synchronized (this) {
                if (downloadTaskService == null) {
                    downloadTaskService = SpringBeanUtil.getBean(DownloadTaskService.class);
                }
            }
        }
        String[] split = signal.split("\r\n\r\n");
        String[] split1 = split[1].split("=");
        String url = split1[1];
        System.out.println(url);
        String decodeUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
        System.out.println(decodeUrl);
        final DownloadTaskEntity entity = new DownloadTaskEntity();
        entity.setUrl(decodeUrl);
        entity.setCreateTime(new Date());
        entity.setDownloadDuration(0L);
        entity.setFinishMediaSegment(0);
        entity.setTotalMediaSegment(0);
        int maxThread = ApplicationStore.getSystemConfig().getDefaultThreadCount();
        entity.setMaxThreadCount(maxThread);
        entity.setSaveFilename(null);
        entity.setStage(DownloadTaskStageEnum.NEW.name());
        entity.setStatus(DownloadTaskStageEnum.NEW.name());
        downloadTaskService.save(entity);
        final DownloadTaskViewModel task = new DownloadTaskViewModel(entity);
        ApplicationStore.getNoFinishedTasks().add(task);
         /*
        // TaskDownloadService 负责整个下载过程
        TaskDownloadThreadManager.getInstance().startDownloadThread(entity);*/
    }
}
