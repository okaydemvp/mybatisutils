package org.mybatis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Repository
@Scope("prototype")
public class UnitService implements AutoCloseable {

    @Autowired
    private UnitServiceDao unitServiceDao;

    private List<Object> addEntitys ;
    private List<Object> updateEntitys ;
    private List<Object> deleteEntitys ;

    public UnitService withDataSource(String dataSource) {
        MultiSourceHolder.setDataSource(dataSource);
        return this;
    }

    public UnitService withDataSource(Enum dataSource) {
        MultiSourceHolder.setDataSource(dataSource.name());
        return this;
    }

    public UnitService(){
        this.addEntitys = new ArrayList<>();
        this.updateEntitys = new ArrayList<>();
        this.deleteEntitys = new ArrayList<>();
    }

    public void add(Object entity) {
        Objects.requireNonNull(entity);
        addEntitys.add(entity);
    }

    public void update(Object entity) {
        Objects.requireNonNull(entity);
        updateEntitys.add(entity);
    }

    public void delete(Object entity) {
        Objects.requireNonNull(entity);
        deleteEntitys.add(entity);
    }

    @Transactional
    public void commit() {
        for (Object entity : addEntitys) {
            unitServiceDao.insert(entity);
        }
        for (Object entity : updateEntitys) {
            unitServiceDao.update(entity);
        }
        for (Object entity : deleteEntitys) {
            unitServiceDao.delete(entity);
        }
        MultiSourceHolder.clearDataSource();
    }

    @Override
    public void close() throws Exception {
    }
}
