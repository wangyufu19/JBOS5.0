package com.jbosframework.transaction.support;

import com.jbosframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * TransactionalMetadata
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionalMetadata {


    private Class<?> targetClass;

    private Method method;

    private Transactional transactional;


    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Transactional getTransactional() {
        return transactional;
    }

    public void setTransactional(Transactional transactional) {
        this.transactional = transactional;
    }

}
