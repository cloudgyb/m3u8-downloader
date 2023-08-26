package com.github.cloudgyb.m3u8downloader.proxy.util;

public interface LRUCache<K, V> {
    V get(K k);

    void put(K k, V v);

    V remove(K k);

    int getSize();

    static <K, V> LRUCache<K, V> getDefault(int cap) {
        return new LRUCacheImpl<>(cap);
    }
}
