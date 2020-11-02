package com.jbosframework.context.annotation;
import com.jbosframework.aop.MethodInterceptor;
import com.jbosframework.context.support.BeanFactoryContext;

import java.lang.reflect.Method;

/**
 * ConfigurationClassInterceptor
 * @author youfu.wang
 * @version 1.0
 */
public class ConfigurationClassInterceptor implements MethodInterceptor {
    private BeanFactoryContext context;

    /**
     * 构造方法
     * @param context
     */
    public ConfigurationClassInterceptor(BeanFactoryContext context){
        this.context=context;
    }
    public Object intercept(Object obj, String method) {
        return null;
    }
}
