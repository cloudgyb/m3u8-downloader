package com.github.cloudgyb.m3u8downloader.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Bean 工具类，用于获取 bean 实例
 *
 * @author geng
 * @since 2023/03/17 16:03:20
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {
    private volatile static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

}
