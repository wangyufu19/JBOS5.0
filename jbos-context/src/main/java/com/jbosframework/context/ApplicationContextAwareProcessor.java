package com.jbosframework.context;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.core.Ordered;

public class ApplicationContextAwareProcessor implements BeanPostProcessor, Ordered {

    private ConfigurableApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    public int getOrder() { return Ordered.HIGHEST_PRECEDENCE;}
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
