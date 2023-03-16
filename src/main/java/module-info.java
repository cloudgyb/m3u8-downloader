module m3u8downloader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.net.http;
    requires org.kordamp.bootstrapfx.core;
    requires m3u8.parser;
    requires org.sqlite;
    requires org.slf4j;

    opens com.github.cloudgyb.m3u8downloader;
    opens com.github.cloudgyb.m3u8downloader.viewcontroller;
    opens com.github.cloudgyb.m3u8downloader.model;
    opens fxml;


    exports com.github.cloudgyb.m3u8downloader;
    exports com.github.cloudgyb.m3u8downloader.log;
    exports com.github.cloudgyb.m3u8downloader.viewcontroller;
    exports com.github.cloudgyb.m3u8downloader.model;
    exports com.github.cloudgyb.m3u8downloader.util;
    exports com.github.cloudgyb.m3u8downloader.domain;
}