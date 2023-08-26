package com.github.cloudgyb.m3u8downloader.proxy.util;

import java.util.HashMap;
import java.util.StringJoiner;

public class LRUCacheImpl<K, V> implements LRUCache<K, V> {
    private final HashMap<K, Node<K, V>> map = new HashMap<>();
    private final Node<K, V> head = new Node<>();
    private final Node<K, V> tail = new Node<>();
    private int size;
    private final int cap;

    public LRUCacheImpl(int cap) {
        this.cap = cap;
        head.next = tail;
        tail.pre = head;
    }

    @Override
    public synchronized V get(K k) {
        Node<K, V> node = map.get(k);
        if (node == null) {
            return null;
        }
        moveToHead(node);
        return node.v;
    }

    @Override
    public synchronized void put(K k, V v) {
        Node<K, V> vNode = map.get(k);
        if (vNode != null) {
            vNode.v = v;
            moveToHead(vNode);
        } else {
            Node<K, V> node = new Node<>(k, v);
            if (size < cap) {
                addToHead(node);
            } else {
                removeLast();
                addToHead(node);
            }
            map.put(k, node);
        }
    }

    private synchronized void removeLast() {
        Node<K, V> lastNode = tail.pre;
        if (lastNode == head)
            return;
        lastNode.pre.next = tail;
        tail.pre = lastNode.pre;
        lastNode.next = null;
        lastNode.pre = null;
        map.remove(lastNode.k);
        size--;
    }

    private void addToHead(Node<K, V> vNode) {
        Node<K, V> t = head.next;
        head.next = vNode;
        vNode.pre = head;
        vNode.next = t;
        if (t != null) {
            t.pre = vNode;
        }
        size++;
    }

    private void moveToHead(Node<K, V> vNode) {
        removeNode(vNode);
        addToHead(vNode);
    }

    public synchronized void removeNode(Node<K, V> node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
        node.pre = node.next = null;
        size--;
    }

    @Override
    public synchronized V remove(K k) {
        Node<K, V> node = map.get(k);
        if (node == null)
            return null;
        removeNode(node);
        map.remove(k);
        return node.v;
    }

    @Override
    public synchronized int getSize() {
        return size;
    }

    private static class Node<K, V> {
        Node<K, V> pre;
        Node<K, V> next;
        K k;
        V v;

        public Node(K k, V v) {
            this.k = k;
            this.v = v;
        }

        public Node() {
        }
    }

    @Override
    public synchronized String toString() {
        StringJoiner stringJoiner = new StringJoiner("->");
        Node<K, V> t = head.next;
        while (t != null) {
            stringJoiner.add(String.valueOf(t.v));
            t = t.next;
        }
        return stringJoiner.toString();
    }
}