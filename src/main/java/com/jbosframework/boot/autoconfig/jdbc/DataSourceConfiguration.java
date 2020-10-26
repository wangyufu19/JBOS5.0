package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnProperty;
import com.jbosframework.boot.context.ConfigurationProperties;

/**
 * DataSourceConfiguration
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourceConfiguration {
    @ConditionalOnClass(org.apache.tomcat.jdbc.pool.DataSource.class)
    @ConditionalOnProperty(
            name="jbos.datasource.type",
            value = "org.apache.tomcat.jdbc.pool.DataSource"
    )
    @ConfigurationProperties(
            prefix = "jbos.datasource.tomcat"
    )
    public static class Tomcat{
        public Tomcat(){

        }
        public org.apache.tomcat.jdbc.pool.DataSource getDataSource(DataSourceProperties properties){
            return null;
        }
    }
}
