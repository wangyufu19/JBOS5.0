package com.jbosframework.context;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.context.support.ApplicationResourceContext;

/**
 * ApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public interface ApplicationContext extends BeanFactory{
	/**
	 * 注册Bean
	 * @param cls
	 */
	public void register(Class<?> cls);
	/**
	 * 注册Bean
	 * @param clses
	 */
	public void register(Class<?>[] clses);
	/**
	 * 扫描注解Bean
	 * @param basePackages
	 */
	public void scan(String basePackages);
	
}
