package com.jbosframework.boot.autoconfig.jdbc;
import javax.sql.DataSource;

/**
 * DataSourceBuilder
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourceBuilder {

    public static DataSourceBuilder create(){
        return new DataSourceBuilder();
    }
    public DataSource build(){
        return new DataSourceWrapper();
    }
}
