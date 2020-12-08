package com.jbosframework.boot.context;
import com.jbosframework.beans.config.BeanBeforeProcessor;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.context.support.InitializeBean;
import com.jbosframework.boot.autoconfig.jdbc.DataSourceProperties;
import com.jbosframework.boot.autoconfig.jdbc.DataSourcePropertiesBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ConfigurationPropertiesChecker
 * @author youfu.wang
 * @version 5.0
 */
public class ConfigurationPropertiesChecker extends InitializeBean implements BeanBeforeProcessor {
    private static final Log log= LogFactory.getLog(ConfigurationPropertiesChecker.class);

    public void process(Object obj,BeanDefinition beanDefinition){
        this.afterProperties(obj);
        ConfigurationProperties configurationProperties=(ConfigurationProperties)beanDefinition.getAnnotation(ConfigurationProperties.class);
        if(configurationProperties!=null){
            DataSourceProperties dataSourceProperties= DataSourcePropertiesBuilder.getInstance().create(this.getApplicationContext(),configurationProperties);
            this.getApplicationContext().putBean(DataSourceProperties.class.getName(),dataSourceProperties);
        }
    }
}
