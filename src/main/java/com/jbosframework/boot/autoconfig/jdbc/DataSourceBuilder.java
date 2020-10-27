package com.jbosframework.boot.autoconfig.jdbc;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * DataSourceBuilder
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourceBuilder {

    public static class TomcatDataSourcePool{

        public TomcatDataSourcePool(){

        }
        /**
         * 创建一个数据源
         * @param properties
         * @return
         */
        public static org.apache.tomcat.jdbc.pool.DataSource build(DataSourceProperties properties){
            PoolProperties p = new PoolProperties();
            p.setUrl(properties.getUrl());
            p.setDriverClassName(properties.getDriverClass());
            p.setUsername(properties.getUsername());
            p.setPassword(properties.getPassword());
            p.setJdbcInterceptors(
                    "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            DataSource datasource = new DataSource();
            datasource.setPoolProperties(p);
            return datasource;
        }
    }

}
