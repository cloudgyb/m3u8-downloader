package io.lindstrom.m3u8.model;

import java.util.List;


public interface Channels {

    int count();

    List<String> objectCodingIdentifiers();

    static Builder builder() {
        return new Builder();
    }

    class Builder extends ChannelsBuilder {

    }

    static Channels of(int count) {
        return builder().count(count).build();
    }

}
