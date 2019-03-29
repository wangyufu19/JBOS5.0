package com.jbosframework.beans.support;
import com.jbosframework.beans.factory.AnnotationScanFactory;
import com.jbosframework.aspectj.support.AspectProxyBeanRegister;
/**
 * AnnotationBeanReaderImpl
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanReaderImpl implements BeanReader{
	private AnnotationScanFactory annotationScanFactory;
	
	/**
	 * 构造方法
	 * @param annotationScanFactory
	 */
	public AnnotationBeanReaderImpl(AnnotationScanFactory annotationScanFactory) {
		this.annotationScanFactory=annotationScanFactory;
	}
	
	/**
	 * 加载Bean定义
	 * @param cls
	 */
	public void loadBeanDefinition(Class<?> cls) {
		if(cls==null) {
			return;
		}
		this.loadAnnotationBean(cls);
	}
	/**
	 * 加载Bean定义
	 * @param clses
	 */
	public void loadBeanDefinitions(Class<?>[] clses) {
		if(clses==null) {
			return;
		}
		for(int i=0;i<clses.length;i++) {
			this.loadBeanDefinition(clses[i]);
		}
	}

	
	/**
	 * 注入注解Bean
	 * @param cls
	 */
	private void loadAnnotationBean(Class<?> cls){
		AnnotationBeanRegister annotationBeanRegister=new AnnotationBeanRegister(this.annotationScanFactory);
		//注册Bean到上下文
		annotationBeanRegister.registerBean(cls);
		//注册切面Bean到上下文
		if(this.annotationScanFactory.getContextConfiguration().getEnableAspectJAutoProxy()){
			AspectProxyBeanRegister aspectProxyBeanRegister=new AspectProxyBeanRegister(this.annotationScanFactory.getAspectProxyBeanContext());
			aspectProxyBeanRegister.registerBean(cls);
		}
	}	

}
