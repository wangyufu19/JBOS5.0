package com.jbosframework.beans.config;

import com.jbosframework.context.support.BeanFactoryContext;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * InjectionMetadata
 * @author youfu.wang
 * @date 2016-12-12
 */
public class InjectionMetadata {
    private BeanFactoryContext beanFactoryContext;
    private boolean isField=true;
    /**
     * 构造方法
     * @param beanFactoryContext
     */
    public InjectionMetadata(BeanFactoryContext beanFactoryContext){
        this.beanFactoryContext=beanFactoryContext;
    }

    /**
     * 是否字段注入
     * @param isField
     */
    public void setIsField(boolean isField){
        this.isField=isField;
    }
    public void inject(Object obj, Field field, Object value){
        int mod = field.getModifiers();
        if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
            return;
        }
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}