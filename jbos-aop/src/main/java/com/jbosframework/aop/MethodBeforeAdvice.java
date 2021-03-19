package com.jbosframework.aop;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;
/**
 * MethodBeforeAdvice
 * @author youfu.wang
 * @version 5.0
 */
public interface MethodBeforeAdvice extends Advice {

    public void before(Object target,Method method,Object[] args) throws Exception;
}
