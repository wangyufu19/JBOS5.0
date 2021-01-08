package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.Joinpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * AspectjMethodBeforeAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class AspectjMethodBeforeAdvice implements MethodBeforeAdvice {
    private static final Log log= LogFactory.getLog(AspectjMethodBeforeAdvice.class);
    private Joinpoint joinpoint;

    public AspectjMethodBeforeAdvice(Joinpoint joinpoint){
        this.joinpoint=joinpoint;
    }
    public void before(Object target, Method method, Object[] args){
        if(method==null){
            return;
        }
    }
}
