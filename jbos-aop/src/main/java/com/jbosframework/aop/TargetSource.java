package com.jbosframework.aop;

public interface TargetSource {

    Class<?> getTargetClass();

    boolean isStatic();

    Object getTarget() throws Exception;

    void releaseTarget(Object var1) throws Exception;
}
