package org.mybatis.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public final class MultiDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return MultiSourceHolder.getDataSource();
    }

}
