package com.jbosframework.aop;

import com.jbosframework.aop.aspectj.AfterAdvice;
import java.lang.reflect.Method;

/**
 * MethodAfterAdvice
 * @author youfu.wang
 * @version 5.0
 */
public interface MethodAfterAdvice extends AfterAdvice {

    public void before(Object target,Method method,Object[] args);

}
