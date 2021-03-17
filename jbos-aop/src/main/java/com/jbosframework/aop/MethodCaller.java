package com.jbosframework.aop;

/**
 * MethodCaller
 * @author youfu.wang
 * @version 5.0
 */
public abstract class MethodCaller implements MethodBeforeAdvice,MethodAfterAdvice{

    public abstract boolean async();

}
