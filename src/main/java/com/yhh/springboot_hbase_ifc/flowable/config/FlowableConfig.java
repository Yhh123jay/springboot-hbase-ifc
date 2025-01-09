package com.yhh.springboot_hbase_ifc.flowable.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


import javax.sql.DataSource;

@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    private final DataSource flowableDataSource;

    private final DataSourceTransactionManager flowableTransactionManager;
    public FlowableConfig(@Qualifier("flowableDataSource") DataSource flowableDataSource, DataSourceTransactionManager flowableTransactionManager) {
        this.flowableDataSource = flowableDataSource;
        this.flowableTransactionManager = flowableTransactionManager;
    }

    @Override
    public void configure(SpringProcessEngineConfiguration ProcessEngineConfiguration) {
        ProcessEngineConfiguration.setDataSource(flowableDataSource);
        ProcessEngineConfiguration.setDatabaseSchema(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngineConfiguration.setTransactionManager(flowableTransactionManager);
        ProcessEngineConfiguration.setActivityFontName("宋体");
        ProcessEngineConfiguration.setLabelFontName("宋体");
        ProcessEngineConfiguration.setAnnotationFontName("宋体");
    }
}
