package com.jbosframework.boot.context;

import com.jbosframework.beans.config.BeanBeforeProcessor;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ConfigurationPropertiesChecker
 * @author youfu.wang
 * @version 1.0
 */
public class ConfigurationPropertiesChecker implements BeanBeforeProcessor {
    private static final Log log= LogFactory.getLog(ConfigurationPropertiesChecker.class);
    private BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public Object process(Object bean){
        return null;
    }
}
