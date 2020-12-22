package com.jbosframework.aop.support;
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
     * 自动代理(true:Cglib库代理;false:JDK动态代理)
     */
    private String autoProxy="false";
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
    /**
     * 得到自动代理值
     * @return
     */
    public String getAutoProxy() {
        return autoProxy;
    }
    /**
     * 设置自动代理值
     * @param autoProxy
     */
    public void setAutoProxy(String autoProxy) {
        this.autoProxy = autoProxy;
    }
}
