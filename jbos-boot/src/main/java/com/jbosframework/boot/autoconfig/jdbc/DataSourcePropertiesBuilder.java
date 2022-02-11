package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DataSourcePropertiesBuilder
 * @author youfu.wang
 * @version 5.0
 */
public class DataSourcePropertiesBuilder {
    private static final Log log= LogFactory.getLog(DataSourcePropertiesBuilder.class);

    public static DataSourcePropertiesBuilder getInstance(){
        return new DataSourcePropertiesBuilder();
    }
    /**
     * 创建一个数据源属性对象
     * @param ctx
     * @param configurationProperties
     * @return
     */
    public DataSourceProperties create(ApplicationContext ctx, ConfigurationProperties configurationProperties){
        DataSourceProperties dataSourceProperties=null;
        if(configurationProperties==null){
            return null;
        }
        String prefix=configurationProperties.prefix();
        String type= StringUtils.replaceNull(ctx.getPropertyValue(DataSourceProperties.DATASOURCE_TYPE));
        String driverClass=StringUtils.replaceNull(ctx.getPropertyValue(DataSourceProperties.DATASOURCE_DRIVERCLASS));

        if (DataSourceProperties.DATASOURCE_TYPE_TOMCAT.equals(type)){
            dataSourceProperties=new TomcatDataSourceProperties();
            dataSourceProperties.setType(type);
            dataSourceProperties.setDriverClass(driverClass);
            loadProperties(ctx,dataSourceProperties,prefix);
        }
        return dataSourceProperties;
    }

    /**
     * 加载数据源属性
     * @param ctx
     * @param dataSourceProperties
     * @param prefix
     */
    private void loadProperties(ApplicationContext ctx,DataSourceProperties dataSourceProperties,String prefix){
        if(prefix.indexOf(".")!=-1){
            Object properties=ctx.getPropertyValue(prefix);
            if(properties!=null){
                dataSourceProperties.load(properties);
            }
            prefix=prefix.substring(0,prefix.lastIndexOf("."));
            this.loadProperties(ctx,dataSourceProperties,prefix);
        }
    }

}
