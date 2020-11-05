package com.jbosframework.beans.factory;

import com.jbosframework.beans.config.BeanDefinition;

/**
 * BeanObjectFactory
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanObjectFactory {

    public Object doCreateBean(BeanDefinition beanDefinition);
}
