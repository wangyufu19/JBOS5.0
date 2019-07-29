package com.jbosframework.context.support;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.factory.AnnotationBeanFactory;
import com.jbosframework.context.ApplicationContextFactory;

/**
 * AnnotationApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationApplicationContext extends BeanFactoryContext implements ApplicationContext{

	private BeanFactory beanFactory;
	/**
	 * 构造方法
	 */
	public AnnotationApplicationContext() {
	}
	/**
	 * 构造方法
	 * @param cls
	 */
	public AnnotationApplicationContext(Class<?> cls) {
		this.beanFactory=new AnnotationBeanFactory(this,cls);
	}
	/**
	 * 构造方法
	 * @param clses
	 */
	public AnnotationApplicationContext(Class<?>[] clses) {
		this.beanFactory=new AnnotationBeanFactory(this,clses);
	}
	/**
	 * 注册Bean
	 * @param cls
	 */
	public void registry(Class<?> cls) {
		this.beanFactory=new AnnotationBeanFactory(this,cls);
		this.afterProperties();
		ApplicationContextFactory.setApplicationContext(this);
	}
	/**
	 * 注册Bean
	 * @param clses
	 */
	public void registry(Class<?>[] clses) {
		this.beanFactory=new AnnotationBeanFactory(this,clses);
		this.afterProperties();
		ApplicationContextFactory.setApplicationContext(this);
	}
}
