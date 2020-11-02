package com.jbosframework.aop;

import java.lang.reflect.Method;

/**
 * MethodInterceptor
 * @author youfu.wang
 * @version 1.0
 */
public interface MethodInterceptor {

    public Object intercept(Object obj, String method);
}
