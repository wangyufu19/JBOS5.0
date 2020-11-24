package com.jbosframework.beans.config;
/**
 * BeanBeforeProcessor
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanBeforeProcessor {

    public Object process(Object bean,BeanDefinition beanDefinition);
}
