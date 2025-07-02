package io.lindstrom.m3u8.model;

import java.util.Optional;


public interface PreloadHint {
    PreloadHintType type();

    String uri();

    Optional<Long> byteRangeStart();

    Optional<Long> byteRangeLength();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends PreloadHintBuilder {

    }

    static PreloadHint of(PreloadHintType type, String uri) {
        return builder().type(type).uri(uri).build();
    }
}
