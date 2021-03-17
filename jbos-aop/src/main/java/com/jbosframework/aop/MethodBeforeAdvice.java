package com.jbosframework.aop;

import com.jbosframework.aop.aspectj.MethodAdvice;

import java.lang.reflect.Method;
/**
 * MethodBeforeAdvice
 * @author youfu.wang
 * @version 5.0
 */
public interface MethodBeforeAdvice extends MethodAdvice {

    public void before(Object target,Method method,Object[] args) throws Exception;
}
