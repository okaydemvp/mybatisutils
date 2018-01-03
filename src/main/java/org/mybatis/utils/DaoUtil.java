package org.mybatis.utils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class DaoUtil {
    public static <T> TwoTuple<Boolean, String> hasPrimaryKey(T t) {
        Objects.requireNonNull(t);
        List<String> primaryKeys = new ArrayList<>();
        String table = t.getClass().getSimpleName();
        Field[] fs = t.getClass().getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            //f.setAccessible(true); //设置些属性是可以访问的
            //System.out.println("name:" + f.getName());
            if (f.getName().toLowerCase().contains("id")) {
                primaryKeys.add(f.getName());
            }
        }
        if (primaryKeys.size() == 0) {
            System.out.println("[primaryKeys#size()]" + primaryKeys.size());
            return TupleUtil.tuple(false, "");
        } else {
            List<String> filterKeys = primaryKeys.stream().filter(s -> s.equalsIgnoreCase(table + "id") || s.equalsIgnoreCase(table + "_id") || s.equalsIgnoreCase("id")).collect(Collectors.toList());
            if (filterKeys.size() == 0) {
                System.out.println("[filterKeys#size()]" + filterKeys.size());
                return TupleUtil.tuple(false, "");
            }
            System.out.println("[filterKeys#size()]" + filterKeys.size());
            if (filterKeys.size() == 1) {
                return TupleUtil.tuple(true, filterKeys.get(0));
            } else {
                if (filterKeys.stream().filter(key -> key.equalsIgnoreCase("id")).collect(Collectors.toList()).size() == 1) {
                    return TupleUtil.tuple(true, "id");
                } else if (filterKeys.stream().filter(key -> key.equalsIgnoreCase(table + "id")).collect(Collectors.toList()).size() == 1) {
                    return TupleUtil.tuple(true, table + "id");
                } else if (filterKeys.stream().filter(key -> key.equalsIgnoreCase(table + "_id")).collect(Collectors.toList()).size() == 1) {
                    return TupleUtil.tuple(true, table + "_id");
                } else {
                    return TupleUtil.tuple(false, "");
                }
            }

        }

    }

    private final static List<String> Numbers = Arrays.asList("Byte", "Short", "Integer", "Long", "BigDecimal");

    public static boolean isNumericTypes(Class clazz) {
        Objects.requireNonNull(clazz);
        if (Numbers.contains(clazz.getSimpleName())) {
            return true;
        }
        return false;
    }

    public static String buildCriteria(Field field, Object object) throws IllegalAccessException {
        Objects.requireNonNull(field);
        Objects.requireNonNull(object);
        String stringCriteria = "";
        Object val = field.get(object);//得到此属性的值
        if (DaoUtil.isNumericTypes(field.getType())) {
            stringCriteria = field.getName() + " = " + val;
        } else {
            if (field.getType().equals(Date.class)) {
                val = DateUtil.getFormatDate((Date) val);
            }
            stringCriteria = field.getName() + " = '" + val + "'";
        }
        return stringCriteria;
    }

    public static String getColumnName(Field field) {
        Objects.requireNonNull(field);
        return field.getName();
    }

    public static String getColumnValue(Field field, Object object) throws IllegalAccessException {
        Objects.requireNonNull(field);
        Objects.requireNonNull(object);
        String stringCriteria = "";
        Object val = field.get(object);//得到此属性的值
        if (DaoUtil.isNumericTypes(field.getType())) {
            stringCriteria = val.toString();
        } else {
            if (field.getType().equals(Date.class)) {
                val = DateUtil.getFormatDate((Date) val);
            }
            stringCriteria = "'" + val + "'";
        }
        return stringCriteria;
    }
}
