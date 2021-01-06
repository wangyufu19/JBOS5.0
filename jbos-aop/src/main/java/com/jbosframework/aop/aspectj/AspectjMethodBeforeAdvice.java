package com.jbosframework.aop.aspectj;

import com.jbosframework.aop.MethodBeforeAdvice;
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
    public void before(Object target, Method method, Object[] args){
        log.info("********target="+target+";method="+this.method+";args="+this.args);
    }
}
