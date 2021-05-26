package com.github.cloudgyb.m3u8downloader;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用上下文，单例模式
 *
 * @author cloudgyb
 * 2021/5/21 18:57
 */
public final class ApplicationContext {
    private static volatile ApplicationContext instance;
    private final ConcurrentHashMap<String, Object> map;

    //单例模式，私有化构造器
    private ApplicationContext() {
        this.map = new ConcurrentHashMap<>();
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            synchronized (ApplicationContext.class) {
                if (instance == null) {
                    instance = new ApplicationContext();
                }
            }
        }
        return instance;
    }

    public void set(String key, Object obj) {
        map.put(key, obj);
    }

    public Object get(String key) {
        return map.get(key);
    }

}
