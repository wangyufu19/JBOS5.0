package com.jbosframework.boot.autoconfig.jdbc;
/**
 * DataSourceProperties
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourceProperties {
    private String type;
    private String driverClass;
    private String url;
    private String username;
    private String password;

    public DataSourceProperties(Object properties){
        this.load(properties);
    }
    private void load(Object properties){

    }
}
