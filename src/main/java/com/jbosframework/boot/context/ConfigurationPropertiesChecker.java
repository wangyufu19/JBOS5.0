package com.jbosframework.boot.context;
import com.jbosframework.beans.config.BeanBeforeProcessor;
import com.jbosframework.boot.autoconfig.jdbc.DataSourceProperties;
import com.jbosframework.boot.autoconfig.jdbc.DataSourcePropertiesBuilder;
import com.jbosframework.context.ApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ConfigurationPropertiesChecker
 * @author youfu.wang
 * @version 1.0
 */
public class ConfigurationPropertiesChecker implements BeanBeforeProcessor {
    private static final Log log= LogFactory.getLog(ConfigurationPropertiesChecker.class);
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public Object process(Object bean){
        Object obj=bean;
        ConfigurationProperties configurationProperties=bean.getClass().getDeclaredAnnotation(ConfigurationProperties.class);
        if(configurationProperties!=null){
            DataSourceProperties dataSourceProperties=DataSourcePropertiesBuilder.create(applicationContext,configurationProperties);
            applicationContext.putBean(DataSourceProperties.class.getName(),dataSourceProperties);
        }
        return obj;
    }
}
