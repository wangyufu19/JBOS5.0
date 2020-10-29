package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.boot.autoconfig.condition.ConditionalOnBean;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnClass;
import com.jbosframework.boot.autoconfig.condition.ConditionalOnProperty;
import com.jbosframework.boot.context.ConfigurationProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;

/**
 * DataSourceConfiguration
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourceConfiguration {
    private static final Log log= LogFactory.getLog(DataSourceConfiguration.class);

    @ConditionalOnClass(org.apache.tomcat.jdbc.pool.DataSource.class)
    @ConditionalOnBean(DataSource.class)
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
        public DataSource getDataSource(DataSourceProperties properties){
           return DataSourceBuilder.create().build(properties);
        }
    }
}
