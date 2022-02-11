package com.jbosframework.beans.support;

import com.jbosframework.beans.config.BeanDefinition;

/**
 * BeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanDefinitionRegistry{

    /**
     * 注册Bean定义对象
     * @param beanDefinition
     */
    public void putBeanDefinition(String name, BeanDefinition beanDefinition);

}
