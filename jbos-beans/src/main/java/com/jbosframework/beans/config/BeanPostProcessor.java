package com.jbosframework.beans.config;

import com.jbosframework.core.Ordered;

/**
 * BeanPostProcessor
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanPostProcessor extends Comparable<BeanPostProcessor>, Ordered {

    default Object postProcessBeforeInitialization(Object bean,BeanDefinition beanDefinition){
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean,BeanDefinition beanDefinition){
        return bean;
    }
}
