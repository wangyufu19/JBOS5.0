package com.jbosframework.beans.config;

import com.jbosframework.core.Order;

/**
 * BeanPostProcessor
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanPostProcessor extends Comparable<BeanPostProcessor>,Order{

    default Object postProcessBeforeInitialization(Object bean,BeanDefinition beanDefinition){
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean,BeanDefinition beanDefinition){
        return bean;
    }
}
