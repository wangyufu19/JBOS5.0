package com.jbosframework.context.support;
import com.jbosframework.beans.support.AnnotationBeanReaderImpl;
import com.jbosframework.beans.support.BeanReader;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.context.AnnotationBeanRegistry;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextFactory;
import com.jbosframework.context.AspectProxyBeanRegister;

/**
 * AnnotationApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationApplicationContext extends BeanFactoryContext implements ApplicationContext{

	private BeanReader beanReader=new AnnotationBeanReaderImpl(this);
	/**
	 * 构造方法
	 */
	public AnnotationApplicationContext() {
		beanReader.addBeanRegistry(new AnnotationBeanRegistry(this));
		if(this.getContextConfiguration().isEnableAspectJAutoProxy()){
			beanReader.addBeanRegistry(new AspectProxyBeanRegister(this.getAspectProxyBeanContext()));
		}
	}
	/**
	 * 构造方法
	 * @param cls
	 */
	public AnnotationApplicationContext(Class<?> cls) {
		beanReader.addBeanRegistry(new AnnotationBeanRegistry(this));
		if(this.getContextConfiguration().isEnableAspectJAutoProxy()){
			beanReader.addBeanRegistry(new AspectProxyBeanRegister(this.getAspectProxyBeanContext()));
		}
		AnnotationScanFactory annotationScanFactory=new AnnotationScanFactory(beanReader);
		annotationScanFactory.scan(cls);
	}
	/**
	 * 构造方法
	 * @param clses
	 */
	public AnnotationApplicationContext(Class<?>[] clses) {
		beanReader.addBeanRegistry(new AnnotationBeanRegistry(this));
		if(this.getContextConfiguration().isEnableAspectJAutoProxy()){
			beanReader.addBeanRegistry(new AspectProxyBeanRegister(this.getAspectProxyBeanContext()));
		}
		AnnotationScanFactory annotationScanFactory=new AnnotationScanFactory(beanReader);
		annotationScanFactory.scan(clses);
	}
	/**
	 * 添加BeanRegistry
	 * @param beanRegistry
	 */
	public void addBeanRegistry(BeanRegistry beanRegistry){
		beanReader.addBeanRegistry(beanRegistry);
	}
	/**
	 * 注册Bean
	 * @param cls
	 */
	public void registry(Class<?> cls) {
		AnnotationScanFactory annotationScanFactory=new AnnotationScanFactory(beanReader);
		annotationScanFactory.scan(cls);
	}
	/**
	 * 注册Bean
	 * @param clses
	 */
	public void registry(Class<?>[] clses) {
		AnnotationScanFactory annotationScanFactory=new AnnotationScanFactory(beanReader);
		annotationScanFactory.scan(clses);
	}
	/**
	 * 刷新容器上下文
	 */
	public void refreshContext(){
		ApplicationContextFactory.setApplicationContext(this);
	}
}
