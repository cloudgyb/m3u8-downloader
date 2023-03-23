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
    requires spring.boot.starter;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.aop;
    requires spring.beans;
    requires spring.core;
    requires spring.tx;
    requires org.mybatis;
    requires com.baomidou.mybatis.plus;
    requires com.baomidou.mybatis.plus.annotation;
    requires com.baomidou.mybatis.plus.core;
    requires com.baomidou.mybatis.plus.extension;
    requires com.baomidou.mybatis.plus.boot.starter;

    opens com.github.cloudgyb.m3u8downloader;
    opens com.github.cloudgyb.m3u8downloader.viewcontroller;
    opens com.github.cloudgyb.m3u8downloader.model;
    opens fxml;


    exports com.github.cloudgyb.m3u8downloader;
    exports com.github.cloudgyb.m3u8downloader.conf;
    exports com.github.cloudgyb.m3u8downloader.log;
    exports com.github.cloudgyb.m3u8downloader.viewcontroller;
    exports com.github.cloudgyb.m3u8downloader.model;
    exports com.github.cloudgyb.m3u8downloader.util;
    exports com.github.cloudgyb.m3u8downloader.m3u8;
    exports com.github.cloudgyb.m3u8downloader.domain;
    exports com.github.cloudgyb.m3u8downloader.event;
    opens com.github.cloudgyb.m3u8downloader.event;
    opens com.github.cloudgyb.m3u8downloader.domain;
    exports com.github.cloudgyb.m3u8downloader.domain.entity;
    exports com.github.cloudgyb.m3u8downloader.domain.service;
    opens com.github.cloudgyb.m3u8downloader.domain.service;
    opens com.github.cloudgyb.m3u8downloader.domain.entity;
}