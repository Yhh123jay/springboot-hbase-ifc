package com.yhh.springboot_hbase_ifc.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.yhh.springboot_hbase_ifc.flowable.mapper",sqlSessionFactoryRef = "flowableSqlSessionFactory")
public class FlowableSourceConfig {
    @Autowired
    @Qualifier("flowableDataSource")
    private DataSource flowableDataSource;

    @Bean(name = "flowableSqlSessionFactory")
    @Primary
    public SqlSessionFactory flowableSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(flowableDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/flowable/*.xml"));
        return sessionFactory.getObject();
    }

    @Bean(name = "flowableTransactionManager")
    @Primary
    public DataSourceTransactionManager flowableTransactionManager(@Qualifier("flowableDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "flowableSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate flowableSqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(flowableSqlSessionFactory());
    }
}
