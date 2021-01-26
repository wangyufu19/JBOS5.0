package com.application.config;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.boot.autoconfig.jdbc.DataSourceBuilder;
import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.context.annotation.Configuration;
import javax.sql.DataSource;

/**
 * DataSourceConfig
 * @author youfu.wang
 * @version 1.0
 */
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "jbos.datasource.tomcat.default")
    public DataSource getDataSource(){
        return DataSourceBuilder.create().build();
    }
}
