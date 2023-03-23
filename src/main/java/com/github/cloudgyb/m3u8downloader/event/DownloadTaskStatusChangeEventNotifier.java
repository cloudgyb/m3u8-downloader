package com.github.cloudgyb.m3u8downloader.event;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author geng
 * @since 2023/03/16 21:10:26
 */
public class DownloadTaskStatusChangeEventNotifier implements EventNotifier {
    public final static DownloadTaskStatusChangeEventNotifier INSTANCE = new DownloadTaskStatusChangeEventNotifier();
    private final CopyOnWriteArraySet<EventAware> eventAwareS = new CopyOnWriteArraySet<>();

    @Override
    public void publish(Event event) {
        eventAwareS.forEach(eventAware -> eventAware.notify(event));
    }

    @Override
    public void subscribe(EventAware eventAware) {
        eventAwareS.add(eventAware);
    }

    @Override
    public void unsubscribe(EventAware eventAware) {
        eventAwareS.remove(eventAware);
    }
}
