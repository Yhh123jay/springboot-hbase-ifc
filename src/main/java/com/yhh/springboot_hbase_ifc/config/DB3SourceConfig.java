package com.yhh.springboot_hbase_ifc.config;

import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.yhh.springboot_hbase_ifc.mapper.clickhouse", sqlSessionFactoryRef = "db3SqlSessionFactory")
public class DB3SourceConfig {
    @Autowired
    @Qualifier("db3DataSource")
    private DataSource db3DataSource;

    @Bean(name = "db3SqlSessionFactory")
    public SqlSessionFactory db3SqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(db3DataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/clickhouse/*.xml"));
        //设置mybatis配置，启用日志输出
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(StdOutImpl.class); // 设置日志实现
        sessionFactory.setConfiguration(configuration);
        return sessionFactory.getObject();
    }

    @Bean(name = "db3TransactionManager")
    public DataSourceTransactionManager db3TransactionManager(@Qualifier("db3DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "db3SqlSessionTemplate")
    public SqlSessionTemplate db3SqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(db3SqlSessionFactory());
    }
}
