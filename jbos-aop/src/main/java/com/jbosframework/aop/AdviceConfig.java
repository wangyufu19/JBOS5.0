package com.jbosframework.aop;

import net.sf.cglib.proxy.MethodInterceptor;

/**
 * AdviceConfig
 * @author youfu.wang
 * @version 5.0
 */
public class AdviceConfig  {
    /**
     * 代理实现类
     */
    private Object target;
    /**
     * 代理接口
     */
    private Class<?>[] proxyInterfaces;
    /**
     * 方法通知器
     */
    private MethodAdvisor methodAdvisor;

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


    public MethodAdvisor getMethodAdvisor() {
        return methodAdvisor;
    }

    public void setMethodAdvisor(MethodAdvisor methodAdvisor) {
        this.methodAdvisor = methodAdvisor;
    }

}
