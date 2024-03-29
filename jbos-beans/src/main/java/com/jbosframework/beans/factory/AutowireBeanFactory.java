package com.jbosframework.beans.factory;

import com.jbosframework.beans.BeansException;

/**
 * AbstractAutowireBeanFactory
 * @author youfu.wang
 * @version 1.0
 */
public interface AutowireBeanFactory extends BeanFactory{
    int AUTOWIRE_NO = 0;
    int AUTOWIRE_BY_NAME = 1;
    int AUTOWIRE_BY_TYPE = 2;
    int AUTOWIRE_CONSTRUCTOR = 3;


    Object initializeBean(Object existingBean, String beanName) throws BeansException;

    void autowireBean(Object existingBean) throws BeansException;

    Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

}
