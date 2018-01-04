package org.mybatis.utils;

import org.apache.ibatis.annotations.SelectProvider;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;

public interface QueryServiceDao {

    @SelectProvider(type = QueryServiceDaoProvider.class, method = "find")
    List<LinkedHashMap<String, Object>> find(Object bean);

    @SelectProvider(type = QueryServiceDaoProvider.class, method = "sqlQuery")
    List<LinkedHashMap<String, Object>> sqlQuery(String sql);

    class QueryServiceDaoProvider {
        public String find(Object bean) throws IllegalAccessException {
            return buildSql(bean, Operation.FIND);
        }

        public String sqlQuery(String sql) {
            return sql;
        }

        private String buildSql(Object bean, Operation operation) throws IllegalAccessException {
            System.out.println("[Operation]" + operation);
            StringBuilder stringCriterias = new StringBuilder();
            StringBuilder stringColumns = new StringBuilder();
            String keyCriteria = "";
            String table = bean.getClass().getSimpleName();
            //Object obj = map.get(bean);
            Class objClass = bean.getClass();
            /*String primaryKey = "";
            TwoTuple<Boolean, String> tuplePrimaryKey = DaoUtil.hasPrimaryKey(map.get(bean));
            if (tuplePrimaryKey.first) {
                System.out.println("[PrimaryKey]" + tuplePrimaryKey.second);
                primaryKey = tuplePrimaryKey.second;
            } else {
                return String.format("%s找不到主键", table);
            }*/

            Field[] fs = objClass.getDeclaredFields();

            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); //设置些属性是可以访问的
                //Object val = f.get(obj);//得到此属性的值
                //System.out.println("name:" + f.getName() + "\t value = " + val);
                if (operation.equals(Operation.FIND)) {
                    stringColumns.append(DaoUtil.getColumnName(f));
                    if (i != fs.length - 1)
                        stringColumns.append(",");
                }

                if (f.get(bean) != null) {
                    switch (operation) {
                        case FIND:
                            stringCriterias.append(DaoUtil.buildCriteria(f, bean) + " AND ");
                            break;
                    }

                }
            }

            String sql = "";

            switch (operation) {
                case FIND:
                    if (stringCriterias.length() > 0) {
                        stringCriterias.delete(stringCriterias.length() - 4, stringCriterias.length());
                    }
                    sql = String.format("%s %s FROM %s WHERE %s;", operation.value(), stringColumns.toString(), table, stringCriterias.toString());
                    break;
            }

            System.out.println("[keyCriteria]" + keyCriteria);
            System.out.println("[stringCriterias]" + stringCriterias.toString());
            System.out.println("[sql]" + sql + "\r\n");
            return sql;
        }
    }

    enum Operation {
        FIND("SELECT");
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
