package com.jbosframework.boot.autoconfig.orm.mybatis;

import com.jbosframework.boot.context.ConfigurationProperties;


@ConfigurationProperties(prefix = "jbos.mybatis")
public class MyBatisProperties {
    private String id="work";
    private String mapperLocations;
    private String logImpl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getLogImpl() {
        return logImpl;
    }

    public void setLogImpl(String logImpl) {
        this.logImpl = logImpl;
    }



}
