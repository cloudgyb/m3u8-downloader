package io.lindstrom.m3u8.model;



import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Media Segment interface
 */

public interface MediaSegment {
    double duration();

    Optional<String> title();

    String uri();

    Optional<ByteRange> byteRange();

    Optional<OffsetDateTime> programDateTime();

    Optional<DateRange> dateRange();

    Optional<SegmentMap> segmentMap();

    Optional<SegmentKey> segmentKey();

    
    default boolean discontinuity() {
        return false;
    }

    Optional<Double> cueOut();

    
    default boolean cueIn() {
        return false;
    }

    
    default boolean gap() {
        return false;
    }

    Optional<Long> bitrate();

    List<PartialSegment> partialSegments();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends MediaSegmentBuilder {
    }
}
