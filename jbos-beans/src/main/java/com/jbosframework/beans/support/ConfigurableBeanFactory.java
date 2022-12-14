package com.jbosframework.beans.support;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanFactory;
/**
 * ConfigurableBeanFactory
 * @author youfu.wang
 * @version 1.0
 */
public interface ConfigurableBeanFactory extends BeanFactory{

    public void putBeanDefinition(String name, BeanDefinition beanDefinition);

    public BeanDefinition getBeanDefinition(String name);

    public void putBeanNameOfType(String name, BeanDefinition beanDefinition);

    public void registerSingletonInstance(String name,Object object);

    public void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
