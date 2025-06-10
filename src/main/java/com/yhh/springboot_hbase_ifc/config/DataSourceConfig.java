package com.yhh.springboot_hbase_ifc.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean(name = "flowableDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.flowable")
    @Primary
    public DataSource flowableDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }

    // 类似地，为db2配置 DataSource、SqlSessionFactory 和 TransactionManager
    @Bean(name = "db2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource db2DataSource() {
        return DataSourceBuilder.create().build();
    }

    // 类似地，为db3配置 DataSource、SqlSessionFactory 和 TransactionManager
    @Bean(name = "db3DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db3")
    public DataSource db3DataSource() {
        return DataSourceBuilder.create().build();
    }
}
