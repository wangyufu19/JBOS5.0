package com.jbosframework.beans.support;
/**
 * BeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public interface BeanRegistry {
    /**
     * 注册Bean
     * @param cls
     */
    public void registry(Class<?> cls);
}
