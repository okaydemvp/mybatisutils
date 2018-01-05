package org.mybatis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public final class QueryService<T> {
    @Autowired
    private QueryServiceDao queryServiceDao;

    public Optional<T> find(T bean) throws IOException {
        Optional<List<T>> list = findList(bean);
        if (list.isPresent()) {
            return Optional.ofNullable(list.get().get(0));
        }
        return Optional.ofNullable(null);
    }

    public Optional<List<T>> findList(T bean) throws IOException {
        List<T> list = Json.toObject(queryServiceDao.find(bean), bean.getClass());
        if (list.size() == 0) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(list);
    }

    public Optional<T> sqlSingleQuery(Class<T> bean, String sql) throws IOException {
        Optional<List<T>> list = sqlQuery(bean, sql);
        if (list.isPresent()) {
            return Optional.ofNullable(list.get().get(0));
        }
        return Optional.ofNullable(null);
    }

    public Optional<List<T>> sqlQuery(Class<T> bean, String sql) throws IOException {
        List<T> list = Json.toObject(queryServiceDao.sqlQuery(sql), bean);
        if (list.size() == 0) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(list);
    }

    public Optional<List<T>> sqlQuery(Class<T> bean, String sql, Map<String, Object> map) throws IOException {
        String param = "";
        String value = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            param = String.format("##%s##", entry.getKey());
            if (DaoUtil.isNumericTypes(entry.getValue().getClass())) {
                value = String.format("%s", entry.getValue());
            } else {
                value = String.format("'%s'", entry.getValue());
            }
            if (sql.compareToIgnoreCase(param) >= 0) {
                sql = sql.replaceAll(param, value);
            }
        }
        System.out.println("[sql]" + sql);
        return sqlQuery(bean, sql);
    }

    public Optional<List<T>> sqlQuery(Class<T> bean, Map<String, Object> map) throws IOException {
        Objects.requireNonNull(map);
        String sql = String.format("SELECT * FROM %s WHERE 1=1 ", bean.getSimpleName());
        StringBuilder stringParam = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (DaoUtil.isNumericTypes(entry.getValue().getClass())) {
                stringParam.append(String.format(" AND %s = %s ", entry.getKey(), entry.getValue()));
            } else {
                stringParam.append(String.format(" AND %s = '%s'", entry.getKey(), entry.getValue()));
            }
        }
        System.out.println("[sql]" + sql + stringParam.toString());
        return sqlQuery(bean, sql + stringParam.toString());
    }

    public Optional<List<T>> sqlQuery(Class<T> bean, Map<String, Object> map, List<String> columnNames) throws IOException {
        Objects.requireNonNull(map);
        Objects.requireNonNull(columnNames);
        String sql = String.format("SELECT %s FROM %s WHERE 1=1 ", String.join(",", columnNames), bean.getSimpleName());
        return sqlQuery(bean, sql, map);
    }

    public Optional<List<T>> sqlQuery(Class<T> bean, Where where) throws IOException {
        Objects.requireNonNull(where);
        String sql = String.format("SELECT * FROM %s WHERE %s", bean.getSimpleName(), where.build());
        System.out.println("[sql]" + sql);
        return sqlQuery(bean, sql);
    }

    public Optional<List<T>> sqlQuery(Class<T> bean, Where where, List<String> columnNames) throws IOException {
        Objects.requireNonNull(where);
        Objects.requireNonNull(columnNames);
        String sql = String.format("SELECT %s FROM %s WHERE %s", String.join(",", columnNames), bean.getSimpleName(), where.build());
        System.out.println("[sql]" + sql);
        return sqlQuery(bean, sql);
    }

    public Optional<List<T>> sqlQuery(Class<T> bean, String sql, Where where) throws IOException {
        Objects.requireNonNull(sql);
        Objects.requireNonNull(where);
        return sqlQuery(bean, sql, where.buildMap());
    }
}
