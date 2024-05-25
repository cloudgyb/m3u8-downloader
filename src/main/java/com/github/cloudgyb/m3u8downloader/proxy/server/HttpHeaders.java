package com.github.cloudgyb.m3u8downloader.proxy.server;

import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http2.Http2Headers;

import java.util.*;
import java.util.stream.Collectors;

public class HttpHeaders implements Headers<String, String, HttpHeaders> {
    private io.netty.handler.codec.http.HttpHeaders httpHeaders;
    private Http2Headers http2Headers;

    public HttpHeaders(io.netty.handler.codec.http.HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public HttpHeaders(Http2Headers http2Headers) {
        this.http2Headers = http2Headers;
    }

    @Override
    public String get(String name) {
        if (httpHeaders != null) {
            return httpHeaders.get(name);
        }
        CharSequence charSequence = http2Headers.get(name);
        if (charSequence == null) {
            return null;
        }
        return String.valueOf(charSequence);
    }

    @Override
    public String get(String name, String defaultValue) {
        String s = get(name);
        return s == null ? defaultValue : s;
    }

    @Override
    public String getAndRemove(String name) {
        String s = get(name);
        if (s != null) {
            if (httpHeaders != null) {
                httpHeaders.remove(name);
            }
            if (http2Headers != null) {
                http2Headers.remove(name);
            }
        }
        return s;
    }

    @Override
    public String getAndRemove(String name, String defaultValue) {
        String s = getAndRemove(name);
        return s == null ? defaultValue : s;
    }

    @Override
    public List<String> getAll(String name) {
        if (httpHeaders != null) {
            return httpHeaders.getAll(name);
        }
        List<CharSequence> all = http2Headers.getAll(name);
        return all.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllAndRemove(String name) {
        List<String> all = getAll(name);
        if (!all.isEmpty()) {
            if (httpHeaders != null) {
                httpHeaders.remove(name);
            }
            if (http2Headers != null) {
                http2Headers.remove(name);
            }
        }
        return all;
    }

    @Override
    public Boolean getBoolean(String name) {
        String s = get(name);
        if (s == null) {
            return null;
        } else if (Boolean.FALSE.toString().equalsIgnoreCase(s)) {
            return false;
        } else if (Boolean.TRUE.toString().equalsIgnoreCase(s)) {
            return true;
        }
        throw new RuntimeException(s + "is not boolean value");
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        Boolean aBoolean = getBoolean(name);
        if (aBoolean == null) {
            return defaultValue;
        }
        return aBoolean;
    }

    @Override
    public Byte getByte(String name) {
        return null;
    }

    @Override
    public byte getByte(String name, byte defaultValue) {
        return 0;
    }

    @Override
    public Character getChar(String name) {
        return null;
    }

    @Override
    public char getChar(String name, char defaultValue) {
        return 0;
    }

    @Override
    public Short getShort(String name) {
        return null;
    }

    @Override
    public short getShort(String name, short defaultValue) {
        return 0;
    }

    @Override
    public Integer getInt(String name) {
        return null;
    }

    @Override
    public int getInt(String name, int defaultValue) {
        return 0;
    }

    @Override
    public Long getLong(String name) {
        return null;
    }

    @Override
    public long getLong(String name, long defaultValue) {
        return 0;
    }

    @Override
    public Float getFloat(String name) {
        return null;
    }

    @Override
    public float getFloat(String name, float defaultValue) {
        return 0;
    }

    @Override
    public Double getDouble(String name) {
        return null;
    }

    @Override
    public double getDouble(String name, double defaultValue) {
        return 0;
    }

    @Override
    public Long getTimeMillis(String name) {
        return null;
    }

    @Override
    public long getTimeMillis(String name, long defaultValue) {
        return 0;
    }

    @Override
    public Boolean getBooleanAndRemove(String name) {
        return null;
    }

    @Override
    public boolean getBooleanAndRemove(String name, boolean defaultValue) {
        return false;
    }

    @Override
    public Byte getByteAndRemove(String name) {
        return null;
    }

    @Override
    public byte getByteAndRemove(String name, byte defaultValue) {
        return 0;
    }

    @Override
    public Character getCharAndRemove(String name) {
        return null;
    }

    @Override
    public char getCharAndRemove(String name, char defaultValue) {
        return 0;
    }

    @Override
    public Short getShortAndRemove(String name) {
        return null;
    }

    @Override
    public short getShortAndRemove(String name, short defaultValue) {
        return 0;
    }

    @Override
    public Integer getIntAndRemove(String name) {
        return null;
    }

    @Override
    public int getIntAndRemove(String name, int defaultValue) {
        return 0;
    }

    @Override
    public Long getLongAndRemove(String name) {
        return null;
    }

    @Override
    public long getLongAndRemove(String name, long defaultValue) {
        return 0;
    }

    @Override
    public Float getFloatAndRemove(String name) {
        return null;
    }

    @Override
    public float getFloatAndRemove(String name, float defaultValue) {
        return 0;
    }

    @Override
    public Double getDoubleAndRemove(String name) {
        return null;
    }

    @Override
    public double getDoubleAndRemove(String name, double defaultValue) {
        return 0;
    }

    @Override
    public Long getTimeMillisAndRemove(String name) {
        return null;
    }

    @Override
    public long getTimeMillisAndRemove(String name, long defaultValue) {
        return 0;
    }

    @Override
    public boolean contains(String name) {
        return false;
    }

    @Override
    public boolean contains(String name, String value) {
        return false;
    }

    @Override
    public boolean containsObject(String name, Object value) {
        return false;
    }

    @Override
    public boolean containsBoolean(String name, boolean value) {
        return false;
    }

    @Override
    public boolean containsByte(String name, byte value) {
        return false;
    }

    @Override
    public boolean containsChar(String name, char value) {
        return false;
    }

    @Override
    public boolean containsShort(String name, short value) {
        return false;
    }

    @Override
    public boolean containsInt(String name, int value) {
        return false;
    }

    @Override
    public boolean containsLong(String name, long value) {
        return false;
    }

    @Override
    public boolean containsFloat(String name, float value) {
        return false;
    }

    @Override
    public boolean containsDouble(String name, double value) {
        return false;
    }

    @Override
    public boolean containsTimeMillis(String name, long value) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<String> names() {
        return null;
    }

    @Override
    public HttpHeaders add(String name, String value) {
        return null;
    }

    @Override
    public HttpHeaders add(String name, Iterable<? extends String> values) {
        return null;
    }

    @Override
    public HttpHeaders add(String name, String... values) {
        return null;
    }

    @Override
    public HttpHeaders addObject(String name, Object value) {
        return null;
    }

    @Override
    public HttpHeaders addObject(String name, Iterable<?> values) {
        return null;
    }

    @Override
    public HttpHeaders addObject(String name, Object... values) {
        return null;
    }

    @Override
    public HttpHeaders addBoolean(String name, boolean value) {
        return null;
    }

    @Override
    public HttpHeaders addByte(String name, byte value) {
        return null;
    }

    @Override
    public HttpHeaders addChar(String name, char value) {
        return null;
    }

    @Override
    public HttpHeaders addShort(String name, short value) {
        return null;
    }

    @Override
    public HttpHeaders addInt(String name, int value) {
        return null;
    }

    @Override
    public HttpHeaders addLong(String name, long value) {
        return null;
    }

    @Override
    public HttpHeaders addFloat(String name, float value) {
        return null;
    }

    @Override
    public HttpHeaders addDouble(String name, double value) {
        return null;
    }

    @Override
    public HttpHeaders addTimeMillis(String name, long value) {
        return null;
    }

    @Override
    public HttpHeaders add(Headers<? extends String, ? extends String, ?> headers) {
        return null;
    }

    @Override
    public HttpHeaders set(String name, String value) {
        return null;
    }

    @Override
    public HttpHeaders set(String name, Iterable<? extends String> values) {
        return null;
    }

    @Override
    public HttpHeaders set(String name, String... values) {
        return null;
    }

    @Override
    public HttpHeaders setObject(String name, Object value) {
        return null;
    }

    @Override
    public HttpHeaders setObject(String name, Iterable<?> values) {
        return null;
    }

    @Override
    public HttpHeaders setObject(String name, Object... values) {
        return null;
    }

    @Override
    public HttpHeaders setBoolean(String name, boolean value) {
        return null;
    }

    @Override
    public HttpHeaders setByte(String name, byte value) {
        return null;
    }

    @Override
    public HttpHeaders setChar(String name, char value) {
        return null;
    }

    @Override
    public HttpHeaders setShort(String name, short value) {
        return null;
    }

    @Override
    public HttpHeaders setInt(String name, int value) {
        return null;
    }

    @Override
    public HttpHeaders setLong(String name, long value) {
        return null;
    }

    @Override
    public HttpHeaders setFloat(String name, float value) {
        return null;
    }

    @Override
    public HttpHeaders setDouble(String name, double value) {
        return null;
    }

    @Override
    public HttpHeaders setTimeMillis(String name, long value) {
        return null;
    }

    @Override
    public HttpHeaders set(Headers<? extends String, ? extends String, ?> headers) {
        return null;
    }

    @Override
    public HttpHeaders setAll(Headers<? extends String, ? extends String, ?> headers) {
        return null;
    }

    @Override
    public boolean remove(String name) {
        return false;
    }

    @Override
    public HttpHeaders clear() {
        return null;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return null;
    }
}
