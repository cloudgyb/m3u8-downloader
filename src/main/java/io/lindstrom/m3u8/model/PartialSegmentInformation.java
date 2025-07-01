package io.lindstrom.m3u8.model;

public interface PartialSegmentInformation {

    double partTargetDuration();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends PartialSegmentInformationBuilder {

    }

    static PartialSegmentInformation of(double partTargetDuration) {
        return builder().partTargetDuration(partTargetDuration).build();
    }
}
