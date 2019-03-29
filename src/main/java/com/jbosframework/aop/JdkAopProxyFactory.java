package com.jbosframework.aop;
import com.jbosframework.aop.AopProxyFactory;
import com.jbosframework.aop.JdkDynamicProxy;
/**
 * DefaultAopProxyFactory
 * @author youfu.wang
 * @version 1.0
 * @date 2016-11-11
 */
public class JdkAopProxyFactory implements AopProxyFactory{
	/**
	 * 代理实现类
	 */
	private Object proxyInstance;
	/**
	 * 代理接口
	 */
	private Class proxiedInterfaces;
	/**
	 * 构造方法
	 */
	public JdkAopProxyFactory(){
		
	}
	/**
	 * 构造方法
	 * @param proxyInstance
	 * @param proxiedInterfaces
	 */
	public JdkAopProxyFactory(Object proxyInstance,Class proxiedInterfaces){
		this.proxyInstance=proxyInstance;
		this.proxiedInterfaces=proxiedInterfaces;
	}
	/**
	 * 得到代理实现类
	 * @return
	 */
	public Object getProxyInstance() {
		return proxyInstance;
	}
	/**
	 * 设置代理实现类
	 * @param proxyInstance
	 */
	public void setProxyInstance(Object proxyInstance) {
		this.proxyInstance = proxyInstance;
	}
	/**
	 * 得到代理接口
	 * @return
	 */
	public Class getProxiedInterfaces() {
		return proxiedInterfaces;
	}
	/**
	 * 设置代理接口
	 * @param proxiedInterfaces
	 */
	public void setProxiedInterfaces(Class proxiedInterfaces) {	
		this.proxiedInterfaces = proxiedInterfaces;
	}
	/**
	 * 创建AOP代理
	 * @return
	 */
	@Override
	public AopProxy createAopProxy(){
		AopProxy aopProxy=new JdkDynamicProxy(proxyInstance,proxiedInterfaces);
		return aopProxy;
	}
}
