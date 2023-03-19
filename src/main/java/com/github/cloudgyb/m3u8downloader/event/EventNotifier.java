package com.github.cloudgyb.m3u8downloader.event;

/**
 * @author geng
 * @since 2023/03/16 20:58:55
 */
public interface EventNotifier {

    void publish(Event event);

    void subscribe(EventAware eventAware);
}
