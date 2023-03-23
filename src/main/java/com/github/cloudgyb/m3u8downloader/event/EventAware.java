package com.github.cloudgyb.m3u8downloader.event;

/**
 * @author geng
 * @since 2023/03/16 21:17:10
 */
public interface EventAware {
    void notify(Event e);
}
