package com.application.config;

import com.jbosframework.beans.annotation.Component;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.core.Ordered;
import com.jbosframework.core.annotaion.Order;

@Component
@Order(100)
public class JbosBeanPostProcessor implements BeanPostProcessor, Ordered {
    private int order=Ordered.HIGHEST_PRECEDENCE+100;

    @Override
    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, BeanDefinition beanDefinition) {
        return null;
    }

    public int getOrder(){
        return order;
    }
}
