package com.jbosframework.beans.support;
/**
 * AbstractBeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public abstract class AbstractBeanRegistry implements BeanRegistry{
    /**
     * 注册Bean
     * @param cls
     */
    public abstract void registry(Class<?> cls);
}
