package org.mybatis.utils;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public interface UnitServiceDao {
    @UpdateProvider(type = UnitServiceDaoProvider.class, method = "update")
    <T> void update(@Param("bean") T bean);

    @DeleteProvider(type = UnitServiceDaoProvider.class, method = "delete")
    <T> void delete(@Param("bean") T bean);

    @InsertProvider(type = UnitServiceDaoProvider.class, method = "insert")
    <T> void insert(@Param("bean") T bean);

    class UnitServiceDaoProvider {
        public String update(Map<String, Object> map) throws IllegalAccessException {
            return buildSql(map, Operation.UPDATE);
        }

        public String delete(Map<String, Object> map) throws IllegalAccessException {
            return buildSql(map, Operation.DELETE);
        }

        public String insert(Map<String, Object> map) throws IllegalAccessException {
            return buildSql(map, Operation.INSERT);
        }

        private String buildSql(Map<String, Object> map, Operation operation) throws IllegalAccessException {
            String bean = "bean";
            System.out.println("[Operation]" + operation);
            StringBuilder stringCriterias = new StringBuilder();
            StringBuilder stringColumns = new StringBuilder();
            String keyCriteria = "";
            String table = map.get(bean).getClass().getSimpleName();
            Object obj = map.get(bean);
            Class objClass = map.get(bean).getClass();
            String primaryKey = "";
            TwoTuple<Boolean, String> tuplePrimaryKey = DaoUtil.hasPrimaryKey(map.get(bean));
            if (tuplePrimaryKey.first) {
                System.out.println("[PrimaryKey]" + tuplePrimaryKey.second);
                primaryKey = tuplePrimaryKey.second;
            } else {
                return String.format("%s找不到主键", table);
            }

            Field[] fs = objClass.getDeclaredFields();

            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); //设置些属性是可以访问的
                //Object val = f.get(obj);//得到此属性的值
                //System.out.println("name:" + f.getName() + "\t value = " + val);

                if (f.get(obj) != null) {
                    switch (operation) {
                        case UPDATE:
                            if (f.getName().equalsIgnoreCase(primaryKey)) {
                                keyCriteria = DaoUtil.buildCriteria(f, obj);
                            } else {
                                stringCriterias.append(DaoUtil.buildCriteria(f, obj) + ",");
                            }
                            break;
                        case DELETE:
                            if (f.getName().equalsIgnoreCase(primaryKey)) {
                                keyCriteria = DaoUtil.buildCriteria(f, obj);
                            } else {
                                stringCriterias.append(DaoUtil.buildCriteria(f, obj) + " AND ");
                            }
                            break;
                        case INSERT:
                            stringColumns.append(DaoUtil.getColumnName(f) + ",");
                            stringCriterias.append(DaoUtil.getColumnValue(f, obj) + ",");
                            break;
                    }

                }
            }

            String sql = "";

            switch (operation) {
                case UPDATE:
                    if (stringCriterias.length() > 0) {
                        stringCriterias.deleteCharAt(stringCriterias.length() - 1);
                    }
                    if (keyCriteria.isEmpty())
                        return "主键必须赋值";
                    sql = String.format("%s %s SET %s WHERE %s", operation.value(), table, stringCriterias.toString(), keyCriteria);
                    break;
                case DELETE:
                    /*if (stringCriterias.length() > 0) {
                        System.out.println("len:" + stringCriterias.length());
                        stringCriterias.delete(stringCriterias.length() - 4, stringCriterias.length());
                    }*/
                    if (keyCriteria.isEmpty())
                        return "主键必须赋值";
                    sql = String.format("%s FROM %s WHERE %s %s", operation.value(), table, stringCriterias.toString(), keyCriteria);
                    break;
                case INSERT:
                    if (stringColumns.length() > 0) {
                        stringColumns.deleteCharAt(stringColumns.length() - 1);
                    }
                    if (stringCriterias.length() > 0) {
                        stringCriterias.deleteCharAt(stringCriterias.length() - 1);
                    }
                    sql = String.format("%s INTO %s (%s) VALUES (%s)", operation.value(), table, stringColumns.toString(), stringCriterias.toString());
                    break;
            }

            System.out.println("[keyCriteria]" + keyCriteria);
            System.out.println("[stringCriterias]" + stringCriterias.toString());
            System.out.println("[sql]" + sql + "\r\n");
            return sql;
        }
    }

    enum Operation {
        UPDATE("UPDATE"), DELETE("DELETE"), INSERT("INSERT");
        private String operation;

        public String value() {
            return this.operation;
        }

        public String getValue() {
            return this.operation;
        }

        Operation(String operation) {
            this.operation = operation;
        }
    }

}
