package com.jbosframework.beans.config;
/**
 * BeanPostProcessor
 * @author youfu.wang
 */
public interface BeanPostProcessor  {

    default Object postProcessBeforeInitialization(Object bean,BeanDefinition beanDefinition) {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean,BeanDefinition beanDefinition){
        return bean;
    }
}
