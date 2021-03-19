package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.MethodAfterAdvice;
import org.aopalliance.intercept.Joinpoint;

import java.lang.reflect.Method;

/**
 * AspectjMethodAfterAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AspectjMethodAfterAdvice implements MethodAfterAdvice {

    private Joinpoint joinpoint;

    public AspectjMethodAfterAdvice(Joinpoint joinpoint){
        this.joinpoint=joinpoint;
    }


    public void after(Object target, Method method, Object[] args)  {
        if(method==null){
            return;
        }
        try {
            joinpoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
