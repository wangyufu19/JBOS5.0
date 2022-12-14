package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DataSourcePropertiesBuilder
 * @author youfu.wang
 * @version 5.0
 */
public class DataSourcePropertiesBuilder {
    private static final Log logger= LogFactory.getLog(DataSourcePropertiesBuilder.class);

    public static DataSourcePropertiesBuilder getInstance(){
        return new DataSourcePropertiesBuilder();
    }

    public DataSourceProperties create(ConfigurableApplicationContext ctx, ConfigurationProperties configurationProperties){
        DataSourceProperties dataSourceProperties=null;
        if(configurationProperties==null){
            return null;
        }
        String prefix=configurationProperties.prefix();
        String type= StringUtils.replaceNull(ctx.getEnvironment().getProperty(DataSourceProperties.DATASOURCE_TYPE));
        String driverClass=StringUtils.replaceNull(ctx.getEnvironment().getProperty(DataSourceProperties.DATASOURCE_DRIVERCLASS));
        dataSourceProperties=new TomcatDataSourceProperties();
        dataSourceProperties.setType(type);
        dataSourceProperties.setDriverClass(driverClass);
        loadProperties(ctx,dataSourceProperties,prefix);
        return dataSourceProperties;
    }

    private void loadProperties(ConfigurableApplicationContext ctx,DataSourceProperties dataSourceProperties,String prefix){
        if(prefix.indexOf(".")!=-1){
            Object properties=ctx.getEnvironment().getProperty(prefix);
            if(properties!=null){
                dataSourceProperties.load(properties);
            }
            prefix=prefix.substring(0,prefix.lastIndexOf("."));
            this.loadProperties(ctx,dataSourceProperties,prefix);
        }
    }

}
