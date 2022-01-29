package com.jbosframework.context.support;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.context.annotation.AnnotationBeanScanner;

/**
 * AnnotationApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationApplicationContext extends AbstractApplicationContext{

	private AnnotationBeanScanner scanner=new AnnotationBeanScanner(this);
	/**
	 * 构造方法
	 */
	public AnnotationApplicationContext() {
	}
	/**
	 * 添加BeanRegistry
	 * @param beanRegistry
	 */
	public void addBeanRegistry(BeanRegistry beanRegistry){
		scanner.addBeanRegistry(beanRegistry);
	}
	/**
	 * 注册Bean
	 * @param cls
	 */
	public void registry(Class<?> cls) {
		scanner.scan(cls);
		this.refresh();
	}
	/**
	 * 注册Bean
	 * @param classes
	 */
	public void registry(Class<?>[] classes) {
		scanner.scan(classes);
		this.refresh();
	}
}
