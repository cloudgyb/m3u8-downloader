package io.lindstrom.m3u8.model;

/**
 * Video resolution interface
 */

public interface Resolution {
    int width();

    int height();

    static Builder builder() {
        return new Builder();
    }

    static Resolution of(int width, int height) {
        return builder()
                .width(width)
                .height(height)
                .build();
    }

    class Builder extends ResolutionBuilder {
    }
}
