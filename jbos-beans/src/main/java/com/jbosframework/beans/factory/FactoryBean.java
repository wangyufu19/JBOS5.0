package com.jbosframework.beans.factory;

/**
 * FactoryBean
 * @author youfu.wang
 * @version 5.0
 */
public interface FactoryBean<T> {
    T getObject() throws Exception;

    Class<?> getObjectType();

    default boolean isSingleton(){return true;}
}
