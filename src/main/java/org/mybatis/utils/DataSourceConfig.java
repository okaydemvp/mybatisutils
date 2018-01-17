/*
package org.mybatis.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "org.mybatis.utils")
public class DataSourceConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource sakilaDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("jdbc.driverClassName"));
        props.put("url", env.getProperty("jdbc.url"));
        props.put("username", env.getProperty("jdbc.username"));
        props.put("password", env.getProperty("jdbc.password"));
        return DruidDataSourceFactory.createDataSource(props);
    }

    @Bean
    public DataSource worldDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("jdbc2.driverClassName"));
        props.put("url", env.getProperty("jdbc2.url"));
        props.put("username", env.getProperty("jdbc2.username"));
        props.put("password", env.getProperty("jdbc2.password"));
        return DruidDataSourceFactory.createDataSource(props);
    }

    @Bean
    public MultiDataSource dataSource() throws Exception {
        MultiDataSource dataSource = new MultiDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("Sakila", sakilaDataSource());
        targetDataSources.put("World", worldDataSource());

        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(sakilaDataSource());
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(MultiDataSource dataSource) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(dataSource);
        return fb.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(MultiDataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

}
*/
