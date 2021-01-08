package com.jbosframework.aop.aspectj;

import com.jbosframework.utils.JBOSClassCaller;
import com.jbosframework.utils.JBOSClassloader;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * AspectJMethodInvocation
 * @author youfu.wang
 * @version 5.0
 */
public class AspectJMethodInvocation implements MethodInvocation {
    private Class<?> target;
    private Method method;
    private Object[] args;

    public AspectJMethodInvocation(Class<?> target, Method method, Object[] args){
        this.target=target;
        this.method=method;
        this.args=args;
    }
    public Method getMethod() {
        return this.method;
    }

    public Object[] getArguments() {
        return this.args;
    }

    public Object proceed() throws Throwable {
        return this.method.invoke(JBOSClassloader.newInstance(this.target),this.args);
    }

    public Object getThis() {
        return this.target;
    }

    public AccessibleObject getStaticPart() {
        return null;
    }
}
