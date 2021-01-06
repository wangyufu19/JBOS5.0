package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * AspectjMethodBeforeAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AspectjMethodBeforeAdvice implements MethodBeforeAdvice {
    private Class<?> targetClass;
    private Method method;

    public void setTargetClass(Class<?> targetClass){
        this.targetClass=targetClass;
    }
    public void setMethod(Method method){
        this.method=method;
    }
    public void before(Object target, Method method, Object[] args){

    }
}
