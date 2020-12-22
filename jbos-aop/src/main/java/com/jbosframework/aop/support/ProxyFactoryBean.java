package com.jbosframework.aop.support;
import com.jbosframework.beans.factory.FactoryBean;
/**
 * ProxyFactoryBean
 * @author youfu.wang
 * @version 5.0
 */
public class ProxyFactoryBean extends AopProxySupport implements FactoryBean {
    private boolean isSingleton=true;
    private Object singletonInstance;

    public boolean isSingleton(){
        return isSingleton;
    }
    public void setSingleton(boolean isSingleton){
        this.isSingleton=isSingleton;
    }

    public Object getObject(){
        if(this.isSingleton){
            return this.getSingletonInstance();
        }else{
           return this.newPrototypeInstance();
        }
    }
    public synchronized Object getSingletonInstance(){
        if (this.singletonInstance==null){
            this.singletonInstance=this.createAopProxy().getProxy();
        }
        return this.singletonInstance;
    }
    public synchronized Object newPrototypeInstance(){
        AopProxySupport aopProxySupport=new AopProxySupport();
        aopProxySupport.setTarget(this.getTarget());
        aopProxySupport.setProxyInterfaces(this.getProxyInterfaces());
        aopProxySupport.setAutoProxy(this.getAutoProxy());
        return aopProxySupport.createAopProxy().getProxy();
    }
    public Class<?> getObjectType() {
        synchronized (this){
            if(this.singletonInstance!=null){
                return this.singletonInstance.getClass();
            }
        }
        Class[] ifcs=this.getProxyInterfaces();
        return ifcs[0];
    }

}