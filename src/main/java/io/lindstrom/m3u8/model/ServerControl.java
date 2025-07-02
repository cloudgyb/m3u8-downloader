package io.lindstrom.m3u8.model;



import java.util.Optional;


public interface ServerControl {
    Optional<Double> canSkipUntil();

    Optional<Boolean> canSkipDateRanges();

    Optional<Double> holdBack();

    Optional<Double> partHoldBack();

    
    default boolean canBlockReload() {
        return false;
    }

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ServerControlBuilder {
    }
}
