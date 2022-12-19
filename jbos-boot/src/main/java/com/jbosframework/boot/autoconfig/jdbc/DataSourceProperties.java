package com.jbosframework.boot.autoconfig.jdbc;
/**
 * DataSourceProperties
 * @author youfu.wang
 * @version 1.0
 */
public abstract class DataSourceProperties {
    public static final String DATASOURCE="jbos.datasource";
    public static final String DATASOURCE_TYPE="jbos.datasource.type";
    public static final String DATASOURCE_TYPE_TOMCAT="org.apache.tomcat.jdbc.pool.DataSource";
    public static final String DATASOURCE_DRIVERCLASS="jbos.datasource.driverClass";
    public static final String DATASOURCE_URL="url";
    public static final String DATASOURCE_USERNAME="username";
    public static final String DATASOURCE_PASSWORD="password";
    private String type;
    private String driverClass;
    private String url;
    private String username;
    private String password;


    public DataSourceProperties(){
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
