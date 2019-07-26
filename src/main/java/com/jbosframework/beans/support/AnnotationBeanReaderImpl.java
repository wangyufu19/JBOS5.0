package com.jbosframework.beans.support;
import com.jbosframework.aspectj.support.AspectProxyBeanRegister;
import com.jbosframework.beans.annotation.AnnotationFilter;
import com.jbosframework.context.ApplicationContext;

/**
 * AnnotationBeanReaderImpl
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanReaderImpl implements BeanReader{
	private ApplicationContext ctx;
	private AnnotationFilter annotationFilter;
	
	/**
	 * 构造方法
	 * @param ctx
	 */
	public AnnotationBeanReaderImpl(ApplicationContext ctx) {
		this.ctx=ctx;
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
	public void setAnnotationFilter(AnnotationFilter annotationFilter){
		this.annotationFilter=annotationFilter;
	}
	
	/**
	 * 注入注解Bean
	 * @param cls
	 */
	private void loadAnnotationBean(Class<?> cls){
		AnnotationBeanRegistry annotationBeanRegister=new AnnotationBeanRegistry(this.ctx);
		annotationBeanRegister.setAnnotationFilter(annotationFilter);
		//注册Bean到上下文
		annotationBeanRegister.registerBean(cls);
		//注册切面Bean到上下文
		if(this.ctx.getContextConfiguration().getEnableAspectJAutoProxy()){
			AspectProxyBeanRegister aspectProxyBeanRegister=new AspectProxyBeanRegister(this.ctx.getAspectProxyBeanContext());
			aspectProxyBeanRegister.registerBean(cls);
		}
	}	

}
