package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.MethodAfterAdvice;
import java.lang.reflect.Method;

/**
 * AspectjMethodAfterAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AspectjMethodAfterAdvice implements MethodAfterAdvice {
    private Object target;
    private String method;
    private Object[] args;

    public void setTarget(Object target){
        this.target=target;
    }
    public void setMethod(String method){
        this.method=method;
    }
    public void setArgs(Object[] args){
        this.args=args;
    }
    public void after(Object target, Method method, Object[] args){

    }
}
