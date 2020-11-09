package com.application.examples.config;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.boot.autoconfig.jdbc.DataSourceBuilder;
import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;

/**
 * DataSourceConfig
 * @author youfu.wang
 * @version 1.0
 */
@Configuration
@Slf4j
public class DataSourceConfig {
    @Bean("default")
    @ConfigurationProperties(prefix = "jbos.datasource.tomcat.default")
    public DataSource getDataSource(){
        return DataSourceBuilder.create().build();
    }
}
