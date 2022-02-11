package com.jbosframework.beans.factory;

import com.jbosframework.beans.config.InjectionMetadata;

import java.lang.reflect.Field;

/**
 * DependencyFactory
 * @author youfu.wang
 * @version 1.0
 */
public interface DependencyFactory {
    InjectionMetadata injectionMetadata=new InjectionMetadata();

    default void inject(Object existingBean, Field field,Object value){
        injectionMetadata.inject(existingBean,field,value);
    }
    void autowireDependency(Object existingBean, Field field);
}
