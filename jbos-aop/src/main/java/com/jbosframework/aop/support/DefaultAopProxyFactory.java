package com.jbosframework.aop.support;

import com.jbosframework.aop.*;

/**
 * DefaultAopProxyFactory
 * @author youfu.wang
 * @version 5.0
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    /**
     * 创建AOP代理
     * @return
     */
    public AopProxy createAopProxy(ProxyConfig proxyConfig){
        AopProxyFactory aopProxyFactory=null;
        if("true".equals(proxyConfig.getAutoProxy())){
            aopProxyFactory=new CglibAopProxyFactory();
        }else{
            aopProxyFactory=new JdkAopProxyFactory();
        }
        return "true".equals(proxyConfig.getAutoProxy())?new CglibProxy(proxyConfig):new JdkDynamicProxy(proxyConfig);
    }
}
