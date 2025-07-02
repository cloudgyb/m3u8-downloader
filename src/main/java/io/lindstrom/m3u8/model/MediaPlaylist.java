package io.lindstrom.m3u8.model;



import java.util.List;
import java.util.Optional;

/**
 * Media Playlist interface
 */

public interface MediaPlaylist extends Playlist {
    int targetDuration();

    
    default long mediaSequence() {
        return 0;
    }

    
    default long discontinuitySequence() {
        return 0;
    }

    
    default boolean ongoing() {
        return true;
    }

    Optional<Boolean> allowCache();

    Optional<PlaylistType> playlistType();

    
    default boolean iFramesOnly() {
        return false;
    }

    List<MediaSegment> mediaSegments();

    Optional<ServerControl> serverControl();

    Optional<PartialSegmentInformation> partialSegmentInformation();

    List<PartialSegment> partialSegments();

    Optional<Skip> skip();

    Optional<PreloadHint> preloadHint();

    List<RenditionReport> renditionReports();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends MediaPlaylistBuilder {
    }
}
