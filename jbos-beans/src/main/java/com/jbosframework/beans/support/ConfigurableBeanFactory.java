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
    /**
     * 注册Bean定义对象
     * @param beanDefinition
     */
    public void putBeanDefinition(String name, BeanDefinition beanDefinition);

    /**
     * 得到Bean定义对象
     * @param name
     */
    public BeanDefinition getBeanDefinition(String name);

    /**
     * 注册Bean
     * @param name
     * @param beanDefinition
     */
    public void putBeanNameOfType(String name, BeanDefinition beanDefinition);

    public void registerSingletonInstance(String name,Object object);

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
