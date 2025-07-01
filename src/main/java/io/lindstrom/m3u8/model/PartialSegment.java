package io.lindstrom.m3u8.model;


import java.util.Optional;


public interface PartialSegment {

    String uri();

    double duration();

    default boolean independent() {
        return false;
    }

    Optional<ByteRange> byterange();

    default boolean gap() {
        return false;
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends PartialSegmentBuilder {
    }
}
