package com.jbosframework.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * MethodAdvisor
 * @author youfu.wang
 * @version 5.0
 */
public abstract class MethodAdvisor implements MethodInterceptor {
    private Object target;
    private String adviceMethod;


    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }


    public String getAdviceMethod() {
        return adviceMethod;
    }

    public void setAdviceMethod(String adviceMethod) {
        this.adviceMethod = adviceMethod;
    }

    public Object doIntercept(Object object, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        Object result = null;
        result = methodProxy.invoke(this.getTarget(),args);
        return result;
    }
}
