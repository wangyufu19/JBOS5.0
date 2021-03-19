package com.jbosframework.aop;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;

/**
 * MethodAfterAdvice
 * @author youfu.wang
 * @version 5.0
 */
public interface MethodAfterAdvice extends Advice {

    public void after(Object target,Method method,Object[] args) throws Exception;

}
