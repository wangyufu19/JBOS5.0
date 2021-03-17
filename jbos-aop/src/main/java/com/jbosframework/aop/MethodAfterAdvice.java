package com.jbosframework.aop;

import com.jbosframework.aop.aspectj.MethodAdvice;
import java.lang.reflect.Method;

/**
 * MethodAfterAdvice
 * @author youfu.wang
 * @version 5.0
 */
public interface MethodAfterAdvice extends MethodAdvice {

    public void after(Object target,Method method,Object[] args) throws Exception;

}
