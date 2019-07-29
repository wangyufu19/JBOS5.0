package com.jbosframework.beans.factory;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.context.ApplicationContext;

/**
 * AnnotationScanFactory
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanFactory implements BeanFactory{
	private ApplicationContext ctx;
	private AnnotationScanFactory annotationScanFactory;
	/**
	 * 构造方法
	 * @param cls
	 */
	public AnnotationBeanFactory(ApplicationContext ctx,Class<?> cls) {
		this.ctx=ctx;
		annotationScanFactory=new AnnotationScanFactory(ctx);
		annotationScanFactory.scan(cls);
	}
	/**
	 * 构造方法
	 * @param clses
	 */
	public AnnotationBeanFactory(ApplicationContext ctx,Class<?>[] clses) {
		this.ctx=ctx;
		annotationScanFactory = new AnnotationScanFactory(ctx);
		annotationScanFactory.scan(clses);
	}
	public boolean containsBean(String name) {
		return ctx.containsBean(name);
	}
	
	public BeanDefinition getBeanDefinition(String name){
		return ctx.getBeanDefinition(name);
	}
	
	public Object getBean(String name) {		
		Object obj=null;
		obj=ctx.getBean(name);
		return obj;
	}
	public <T> T getBean(String name,Class<T> requiredType){
		return ctx.getBean(name,requiredType);
	}
	
	public boolean isPrototype(String name) {
		return ctx.isPrototype(name);
	}
	
	public boolean isSingleton(String name) {
		return ctx.isSingleton(name);
	}
	
	public void destroy(){
		ctx.destroy();
	}
	/**
	 * 设置Bean对象
	 * @param name
	 * @param obj
	 */
	public void putBean(String name,Object obj){
		this.ctx.putBean(name,obj);
	}
	public void putBeanDefinition(BeanDefinition beanDefinition){
		this.ctx.putBeanDefinition(beanDefinition);
	}

}
