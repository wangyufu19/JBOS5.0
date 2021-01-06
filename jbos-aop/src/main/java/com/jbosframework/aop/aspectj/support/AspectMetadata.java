package com.jbosframework.aop.aspectj.support;

import com.jbosframework.aop.MethodAfterAdvice;
import com.jbosframework.aop.MethodBeforeAdvice;

/**
 * AspectMetadata
 * @author youfu.wang
 * @version 1.0
 */
public class AspectMetadata {
    //切面类
    private Class<?> aspectClass;
    //连接点
    private String pointcut;
    //完成前
    private MethodBeforeAdvice methodBeforeAdvice;
    //完成后
    private MethodAfterAdvice methodAfterAdvice;

    public Class<?> getAspectClass() {
        return aspectClass;
    }

    public void setAspectClass(Class<?> aspectClass) {
        this.aspectClass = aspectClass;
    }

    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }
    public MethodBeforeAdvice getMethodBeforeAdvice() {
        return methodBeforeAdvice;
    }

    public void setMethodBeforeAdvice(MethodBeforeAdvice methodBeforeAdvice) {
        this.methodBeforeAdvice = methodBeforeAdvice;
    }

    public MethodAfterAdvice getMethodAfterAdvice() {
        return methodAfterAdvice;
    }

    public void setMethodAfterAdvice(MethodAfterAdvice methodAfterAdvice) {
        this.methodAfterAdvice = methodAfterAdvice;
    }
}
