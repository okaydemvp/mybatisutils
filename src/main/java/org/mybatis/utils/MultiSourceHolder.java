package org.mybatis.utils;

import java.util.Objects;

public final class MultiSourceHolder {
    private static final ThreadLocal<String> THREAD_DATA_SOURCE = new ThreadLocal<>();

    public static String getDataSource() {
        return THREAD_DATA_SOURCE.get();
    }

    public static void setDataSource(String dataSource) {
        Objects.requireNonNull(dataSource);
        THREAD_DATA_SOURCE.set(dataSource);
    }

    public static void clearDataSource() {
        THREAD_DATA_SOURCE.remove();
    }
}
