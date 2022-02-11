package com.jbosframework.beans.factory;

import com.jbosframework.beans.config.BeanDefinition;

/**
 * ImportBeanFactory
 * @author youfu.wang
 * @version 5.0
 */
public interface ImportFactory {

    public void importBean(Object bean, BeanDefinition beanDefinition);
}
