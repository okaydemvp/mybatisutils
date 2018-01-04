package org.mybatis.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Where {
    private final List<String> wheres = new ArrayList<>();
    private final String LPARENTHESE = "(";
    private final String RPARENTHESE = ")";
    private final String OR = "OR";

    public Where in(String criteriaName, List<String> criteriaValue) {
        Objects.requireNonNull(criteriaName);
        Objects.requireNonNull(criteriaValue);
        wheres.add(String.format("%s IN ('%s') ", criteriaName, String.join("','", criteriaValue)));
        return this;
    }

    public Where equals(String criteriaName, Object criteriaValue) {
        Objects.requireNonNull(criteriaName);
        Objects.requireNonNull(criteriaValue);
        if (DaoUtil.isNumericTypes(criteriaValue.getClass())) {
            wheres.add(String.format("%s = %s ", criteriaName, criteriaValue));
        } else {
            wheres.add(String.format("%s = '%s' ", criteriaName, criteriaValue));
        }
        return this;
    }

    public Where like(String criteriaName, Object object) {
        Objects.requireNonNull(criteriaName);
        Objects.requireNonNull(object);
        if (DaoUtil.isNumericTypes(object.getClass())) {
            wheres.add(String.format("%s LIKE %s ", criteriaName, object));
        } else {
            wheres.add(String.format("%s LIKE '%s' ", criteriaName, object));
        }
        return this;
    }

    public <A, B> Where between(String criteriaName, TwoTuple<A, B> criteriaValue) {
        Objects.requireNonNull(criteriaName);
        Objects.requireNonNull(criteriaValue);
        if (Objects.equals(criteriaValue.first, criteriaValue.second) != true)
            throw new RuntimeException("TwoTuple<A, B> object,A与B的类型必须相同");
        if (DaoUtil.isNumericTypes(criteriaValue.first.getClass())) {
            wheres.add(String.format("%s BETWEEN %s AND %s ", criteriaName, criteriaValue.first, criteriaValue.second));
        } else {
            wheres.add(String.format("%s BETWEEN '%s' AND '%s' ", criteriaName, criteriaValue.first, criteriaValue.second));
        }
        return this;
    }

    public Where or() {
        wheres.add("OR");
        return this;
    }

    public Where lParenthese() {
        wheres.add("(");
        return this;
    }

    public Where rParenthese() {
        wheres.add(")");
        return this;
    }

    protected String build() {
        String sql = String.join(" AND ", wheres);
        sql = sql.replaceAll(" AND OR AND", OR);
        sql = sql.replaceAll("\\( AND ", LPARENTHESE);
        sql = sql.replaceAll("  AND \\)", RPARENTHESE);
        System.out.println("[where]" + sql);
        return sql;
    }
}
