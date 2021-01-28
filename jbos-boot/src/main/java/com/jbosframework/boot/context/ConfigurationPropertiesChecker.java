package com.jbosframework.boot.context;

import com.jbosframework.beans.config.BeanBeforeProcessor;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.boot.autoconfig.jdbc.DataSourcePropertiesBuilder;
import com.jbosframework.context.ApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ConfigurationPropertiesChecker
 * @author youfu.wang
 * @version 5.0
 */
public class ConfigurationPropertiesChecker  implements BeanBeforeProcessor {
    private static final Log log= LogFactory.getLog(ConfigurationPropertiesChecker.class);
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }


    public void process(Object obj,BeanDefinition beanDefinition){
        ConfigurationProperties configurationProperties=(ConfigurationProperties)beanDefinition.getAnnotation(ConfigurationProperties.class);
        if(configurationProperties!=null){
           DataSourcePropertiesBuilder.getInstance().create(this.applicationContext,configurationProperties);
        }
    }
}
