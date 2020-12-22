package com.jbosframework.aop.support;

import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.AopProxyFactory;
/**
 * AopProxySupport
 * @author youfu.wang
 * @version 5.0
 */
public class AopProxySupport extends ProxyConfig{

    private AopProxyFactory aopProxyFactory=new DefaultAopProxyFactory();

    /**
     * 设置AopProxyFactory
     * @param aopProxyFactory
     */
    public void setAopProxyFactory(AopProxyFactory aopProxyFactory){
        this.aopProxyFactory=aopProxyFactory;
    }

    /**
     * 创建AOP代理
     * @return
     */
    public AopProxy createAopProxy(){
        return aopProxyFactory.createAopProxy(this);
    }
}
