package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.utils.StringUtils;

/**
 * DataSourcePropertiesBuilder
 * @author youfu.wang
 * @version 1.0
 */
public class DataSourcePropertiesBuilder {
    /**
     * 创建一个数据源属性对象
     * @param ctx
     * @param configurationProperties
     * @return
     */
    public static DataSourceProperties create(ApplicationContext ctx, ConfigurationProperties configurationProperties){
        DataSourceProperties dataSourceProperties=null;
        if(configurationProperties==null){
            return null;
        }
        String prefix=configurationProperties.prefix();
        String type= StringUtils.replaceNull(ctx.getContextConfiguration().getContextProperty(DataSourceProperties.DATASOURCE_TYPE));
        String driverClass=StringUtils.replaceNull(ctx.getContextConfiguration().getContextProperty(DataSourceProperties.DATASOURCE_DRIVERCLASS));
        Object properties=ctx.getContextConfiguration().getContextProperty(configurationProperties.prefix());
        if (DataSourceProperties.DATASOURCE_TYPE_TOMCAT.equals(type)){
            dataSourceProperties=new TomcatDataSourceProperties();
        }
        if(dataSourceProperties!=null){
            dataSourceProperties.setType(type);
            dataSourceProperties.setDriverClass(driverClass);
            dataSourceProperties.load(properties);
        }
        return dataSourceProperties;
    }

}
