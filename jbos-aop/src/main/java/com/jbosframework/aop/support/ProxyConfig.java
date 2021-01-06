package com.jbosframework.aop.support;

import com.jbosframework.aop.MethodAfterAdvice;
import com.jbosframework.aop.MethodBeforeAdvice;
/**
 * ProxyConfig
 * @author youfu.wang
 * @version 5.0
 */
public class ProxyConfig {
    /**
     * 代理实现类
     */
    private Object target;
    /**
     * 代理接口
     */
    private Class<?>[] proxyInterfaces;
    /**
     * 完成前通知
     */
    private MethodBeforeAdvice methodBeforeAdvice;
    /**
     * 完成后通知
     */
    private MethodAfterAdvice methodAfterAdvice;
    /**
     * 得到代理实现类
     * @return
     */
    public Object getTarget() {
        return target;
    }
    /**
     * 设置代理实现类
     * @param target
     */
    public void setTarget(Object target) {
        this.target = target;
    }
    /**
     * 得到代理接口
     * @return
     */
    public Class<?>[] getProxyInterfaces() {
        return proxyInterfaces;
    }
    /**
     * 设置代理接口
     * @param proxyInterfaces
     */
    public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }

    public MethodBeforeAdvice getMethodBeforeAdvice() {
        return methodBeforeAdvice;
    }

    public void setMethodBeforeAdvice(MethodBeforeAdvice methodBeforeAdvice) {
        this.methodBeforeAdvice = methodBeforeAdvice;
    }

    public MethodAfterAdvice getMethodAfterAdvice() {
        return methodAfterAdvice;
    }

    public void setMethodAfterAdvice(MethodAfterAdvice methodAfterAdvice) {
        this.methodAfterAdvice = methodAfterAdvice;
    }
}
