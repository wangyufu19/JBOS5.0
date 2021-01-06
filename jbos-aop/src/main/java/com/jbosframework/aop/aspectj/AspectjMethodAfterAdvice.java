package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.MethodAfterAdvice;
import java.lang.reflect.Method;

/**
 * AspectjMethodAfterAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AspectjMethodAfterAdvice implements MethodAfterAdvice {
    private Class<?> targetClass;
    private Method method;

    public void setTargetClass(Class<?> targetClass){
        this.targetClass=targetClass;
    }
    public void setMethod(Method method){
        this.method=method;
    }
    public void after(Object target, Method method, Object[] args){

    }
}
