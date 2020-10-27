package com.jbosframework.boot.autoconfig.jdbc;
/**
 * DataSourceProperties
 * @author youfu.wang
 * @version 1.0
 */
public abstract class DataSourceProperties {
    public static final String DATASOURCE_TYPE="jbos.datasource.type";
    public static final String DATASOURCE_DRIVERCLASS="jbos.datasource.driverClass";
    private String type;
    private String driverClass;
    private String url;
    private String username;
    private String password;

    public DataSourceProperties(){
    }
    public abstract void load(Object properties);
}
