module m3u8downloader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.net.http;
    requires org.kordamp.bootstrapfx.core;
    //requires m3u8.parser;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;
    requires java.compiler;
    opens com.github.cloudgyb.m3u8downloader;
    opens com.github.cloudgyb.m3u8downloader.viewcontroller;
    opens com.github.cloudgyb.m3u8downloader.model;
    opens fxml to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.event to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.domain to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.domain.service to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.domain.entity to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.domain.dao to javafx.fxml;


    exports com.github.cloudgyb.m3u8downloader;
    exports com.github.cloudgyb.m3u8downloader.conf;
    exports com.github.cloudgyb.m3u8downloader.log;
    exports com.github.cloudgyb.m3u8downloader.viewcontroller;
    exports com.github.cloudgyb.m3u8downloader.model;
    exports com.github.cloudgyb.m3u8downloader.util;
    exports com.github.cloudgyb.m3u8downloader.m3u8;
    exports com.github.cloudgyb.m3u8downloader.domain;
    exports com.github.cloudgyb.m3u8downloader.event;
    exports com.github.cloudgyb.m3u8downloader.domain.entity;
    exports com.github.cloudgyb.m3u8downloader.domain.service;
    exports com.github.cloudgyb.m3u8downloader.database;
    exports com.github.cloudgyb.m3u8downloader.domain.dao;

    uses org.sqlite.JDBC;
}