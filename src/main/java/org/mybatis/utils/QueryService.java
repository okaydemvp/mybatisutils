package org.mybatis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class QueryService {
    @Autowired
    private QueryServiceDao queryServiceDao;

    public <T> Optional<T> find(T bean) {
        Objects.requireNonNull(bean);
        System.out.println("[bean]" + bean.getClass());
        return Optional.ofNullable(Json.copyObject(queryServiceDao.find(bean), bean.getClass()));
    }

    public <T> Optional<T> find(Class<T> clazz, Function<T, T> function) throws IllegalAccessException, InstantiationException {
        return find(function.apply(clazz.newInstance()));
    }
}
