package org.mybatis.utils;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Objects;

public final class Configuration {

    private static Configuration instance = null;
    private final String CONFIGLOCATION = "applicationContext.xml";

    public synchronized static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public synchronized static Configuration getInstance(String configLocation) {
        Objects.requireNonNull(configLocation);
        if (instance == null) {
            instance = new Configuration(configLocation);
        }
        return instance;
    }

    private Configuration() {
        initApplicationContext();
    }

    private Configuration(String configLocation) {
        Objects.requireNonNull(configLocation);
        initApplicationContext(configLocation);
    }

    private void initApplicationContext() {
        new FileSystemXmlApplicationContext(String.format("classpath:%s", CONFIGLOCATION));
    }

    private void initApplicationContext(String configLocation) {
        Objects.requireNonNull(configLocation);
        new FileSystemXmlApplicationContext(String.format("classpath:%s", configLocation));
    }
}
