package com.jbosframework.beans.factory;

import com.jbosframework.beans.BeansException;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ConfigurableListableBeanFactory var1) throws BeansException;
}
