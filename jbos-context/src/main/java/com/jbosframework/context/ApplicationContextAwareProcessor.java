package com.jbosframework.context;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;

public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private ConfigurableApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext){
        this.applicationContext=(ConfigurableApplicationContext)applicationContext;
    }
    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition){
        this.invokeInterfaces(bean);
        return bean;
    }
    private void invokeInterfaces(Object bean){
        if(bean instanceof ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
        }
    }
}
