package com.jacky.springbootdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jacky
 * @time 2021-01-23 10:43
 * @discription
 */
public class RoutingDataSourceConfiguration {

    @Primary
    @Bean
    DataSource dataSource(
            @Autowired @Qualifier("masterDataSource") DataSource masterDataSource,
            @Autowired @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        RoutingDataSource ds = new RoutingDataSource();
        Map<Object ,Object> dataSourceMap = new HashMap<>(10);
        dataSourceMap.put("masterDataSource", masterDataSource);
        dataSourceMap.put("slaveDataSource", slaveDataSource);
        // 关联两个DataSource:
        ds.setTargetDataSources(dataSourceMap);
        // 默认使用masterDataSource:
        ds.setDefaultTargetDataSource(masterDataSource);
        return ds;
    }

    @Bean
    JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    DataSourceTransactionManager dataSourceTransactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

/**
 * 一个最终的@Primary标注的DataSource，它采用Spring提供的AbstractRoutingDataSource;
 * RoutingDataSource本身并不是真正的DataSource，它通过Map关联一组DataSource.
 */
class RoutingDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceContext.getDataSourceRoutingKey();
    }

    /**
     * 确认是否真的切换了DataSource，可以覆写determineTargetDataSource()方法并打印出DataSource的名称：
     * @return
     */
    @Override
    protected DataSource determineTargetDataSource() {
        DataSource ds = super.determineTargetDataSource();
        logger.info("determin target datasource: {}", ds);
        return ds;
    }
}