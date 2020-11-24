package com.jbosframework.boot.autoconfig.jdbc;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.DataSource;
/**
 * DataSourcePoolBuilder
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourcePoolBuilder {

    public static class TomcatDataSourcePool{

        public TomcatDataSourcePool(){

        }
        /**
         * 创建一个数据源
         * @param properties
         * @return
         */
        public static org.apache.tomcat.jdbc.pool.DataSource build(DataSourceProperties properties){
            DataSource datasource = new DataSource();
            TomcatDataSourceProperties tomcatDataSourceProperties=(TomcatDataSourceProperties)properties;
            PoolProperties p = new PoolProperties();
            p.setUrl(tomcatDataSourceProperties.getUrl());
            p.setDriverClassName(tomcatDataSourceProperties.getDriverClass());
            p.setUsername(tomcatDataSourceProperties.getUsername());
            p.setPassword(tomcatDataSourceProperties.getPassword());
            p.setTestWhileIdle(tomcatDataSourceProperties.isTestWhileIdle());
            p.setTestOnBorrow(tomcatDataSourceProperties.isTestOnBorrow());
            p.setValidationQuery(tomcatDataSourceProperties.getValidationQuery());
            p.setTestOnReturn(tomcatDataSourceProperties.isTestOnReturn());
            p.setValidationInterval(tomcatDataSourceProperties.getValidationInterval());
            p.setTimeBetweenEvictionRunsMillis(tomcatDataSourceProperties.getTimeBetweenEvictionRunsMillis());
            p.setMaxActive(tomcatDataSourceProperties.getMaxActive());
            p.setMinIdle(tomcatDataSourceProperties.getMinIdle());
            p.setInitialSize(tomcatDataSourceProperties.getInitialSize());
            p.setMaxWait(tomcatDataSourceProperties.getMaxWait());
            p.setRemoveAbandonedTimeout(tomcatDataSourceProperties.getRemoveAbandonedTimeout());
            p.setMinEvictableIdleTimeMillis(tomcatDataSourceProperties.getMinEvictableIdleTimeMillis());
            p.setLogAbandoned(tomcatDataSourceProperties.isLogAbandoned());
            p.setRemoveAbandoned(tomcatDataSourceProperties.isRemoveAbandoned());
            p.setJdbcInterceptors(
                    "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

            datasource.setPoolProperties(p);
            return datasource;
        }
    }
}
