package com.jbosframework.beans.support;

import com.jbosframework.beans.config.BeanDefinition;

import java.util.List;

/**
 * BeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanDefinitionRegistry{

    void putBeanDefinition(String beanName,BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanName);

    List<String> getBeanDefinitionNames();

    public boolean containsBean(String name);
}
