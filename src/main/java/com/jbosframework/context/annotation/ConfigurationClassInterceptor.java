package com.jbosframework.context.annotation;
import com.jbosframework.aop.MethodInterceptor;
import com.jbosframework.context.support.BeanFactoryContext;
import com.jbosframework.core.JBOSClassCaller;

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

    /**
     * 拦截对象的方法
     * @param obj
     * @param method
     * @return
     */
    public Object intercept(Object obj, String method) {
        return JBOSClassCaller.call(obj,method);
    }
}
