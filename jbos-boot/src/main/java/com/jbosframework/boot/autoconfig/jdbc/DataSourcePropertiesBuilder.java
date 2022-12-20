package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.utils.TypeConverter;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
        if (DataSourceProperties.DATASOURCE_TYPE_TOMCAT.equals(type)){
            dataSourceProperties=new TomcatDataSourceProperties();
        }
        dataSourceProperties.setType(type);
        dataSourceProperties.setDriverClass(driverClass);
        Field[] fields=dataSourceProperties.getClass().getSuperclass().getDeclaredFields();
        loadProperties(ctx,dataSourceProperties,fields,prefix);
        fields=dataSourceProperties.getClass().getDeclaredFields();
        loadProperties(ctx,dataSourceProperties,fields,prefix);
        return dataSourceProperties;
    }

    private void loadProperties(ConfigurableApplicationContext ctx,DataSourceProperties dataSourceProperties,Field[] fields,String prefix){
        for(Field field:fields){
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                this.loadProperties(ctx,dataSourceProperties,field,prefix);
            }
        }
    }
    private void loadProperties(ConfigurableApplicationContext ctx,DataSourceProperties dataSourceProperties,Field field,String prefix){
        if(prefix.indexOf(".")!=-1){
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                Object value= TypeConverter.convert(field.getType().getName(),ctx.getEnvironment().getProperty(prefix+"."+field.getName()));
                if(StringUtils.isNotNUll(value)){
                    InjectionMetadata.inject(dataSourceProperties,field,value);
                }
            }
            prefix=prefix.substring(0,prefix.lastIndexOf("."));
            this.loadProperties(ctx,dataSourceProperties,field,prefix);
        }
    }
}
