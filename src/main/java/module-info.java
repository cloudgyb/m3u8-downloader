module m3u8downloader {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.net.http;
    requires java.security.jgss;
    requires jdk.crypto.ec;
    requires jdk.crypto.cryptoki;
    requires jdk.security.auth;
    requires jdk.httpserver;
    requires org.kordamp.bootstrapfx.core;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;
    requires java.compiler;
    requires ch.qos.logback.classic;
    requires com.fasterxml.jackson.databind;
    opens com.github.cloudgyb.m3u8downloader;
    opens com.github.cloudgyb.m3u8downloader.viewcontroller;
    opens com.github.cloudgyb.m3u8downloader.model;
    opens fxml to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.event to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.domain to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.domain.service to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.domain.entity to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.domain.dao to javafx.fxml;
    opens com.github.cloudgyb.m3u8downloader.signal to com.fasterxml.jackson.databind;

    exports com.github.cloudgyb.m3u8downloader;
    exports com.github.cloudgyb.m3u8downloader.conf;
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
    exports com.github.cloudgyb.m3u8downloader.signal;

    uses org.sqlite.JDBC;
    uses ch.qos.logback.classic.spi.LogbackServiceProvider;
}