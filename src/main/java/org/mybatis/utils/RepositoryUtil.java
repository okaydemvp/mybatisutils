package org.mybatis.utils;

public final class RepositoryUtil {
    public static final QueryService createQueryService() {
        return SpringContext.getBean(QueryService.class);
    }

    public static final UnitService createUnitService() {
        return SpringContext.getBean(UnitService.class);
    }
}
