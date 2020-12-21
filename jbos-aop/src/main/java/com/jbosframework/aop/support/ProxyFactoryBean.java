package com.jbosframework.aop.support;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.AopProxyFactory;
import com.jbosframework.utils.JBOSClassloader;
/**
 * ProxyFactoryBean
 * @author youfu.wang
 * @version 1.0
 */
public class ProxyFactoryBean {
    /**
     * 代理实现类
     */
    private Object target;
    /**
     * 代理接口
     */
    private String proxyInterface;
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
    public String getProxyInterface() {
        return proxyInterface;
    }
    /**
     * 设置代理接口
     * @param proxyInterface
     */
    public void setProxyInterface(String proxyInterface) {
        this.proxyInterface = proxyInterface;
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
    /**
     * 得到AOP代理
     * @param proxyInstance 代理实现类实例
     * @return
     */
    private AopProxy getAopProxy(Object proxyInstance){
        if("true".equals(this.getAutoProxy())){
            AopProxyFactory aopProxyFactory=BeanAopProxyFactory.createCglibAopProxyFactory();
            aopProxyFactory.setProxyInstance(proxyInstance);
            return aopProxyFactory.createAopProxy();
        }else{
            Class<?> proxiedInterfaces=null;
            try {
                proxiedInterfaces= JBOSClassloader.loadClass(this.getProxyInterface());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            AopProxyFactory aopProxyFactory=BeanAopProxyFactory.createJdkAopProxyFactory();
            aopProxyFactory.setProxyInstance(proxyInstance);
            aopProxyFactory.setProxiedInterfaces(proxiedInterfaces);
            return aopProxyFactory.createAopProxy();
        }
    }
    /**
     * 得到代理Bean
     * @return
     */
    public Object getProxyBean(){
        AopProxy aopProxy=getAopProxy(this.getTarget());
        return aopProxy.createProxy();
    }
}