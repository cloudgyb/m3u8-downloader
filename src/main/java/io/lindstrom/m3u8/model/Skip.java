package io.lindstrom.m3u8.model;

import java.util.List;


public interface Skip {

    long skippedSegments();

    List<String> recentlyRemovedDateRanges();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends SkipBuilder {

    }

    static Skip of(int skippedSegments) {
        return builder().skippedSegments(skippedSegments).build();
    }
}
