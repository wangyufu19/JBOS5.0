package com.jbosframework.context.support;
import com.jbosframework.beans.support.AnnotationBeanReaderImpl;
import com.jbosframework.beans.support.BeanReader;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.context.AnnotationBeanRegistry;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextFactory;
import com.jbosframework.context.configuration.Configuration;

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
	}
	/**
	 * 构造方法
	 */
	public AnnotationApplicationContext(Configuration configuration) {
		super(configuration);
		beanReader.addBeanRegistry(new AnnotationBeanRegistry(this));
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
		this.autowired();
	}
	/**
	 * 注册Bean
	 * @param classes
	 */
	public void registry(Class<?>[] classes) {
		AnnotationScanFactory annotationScanFactory=new AnnotationScanFactory(beanReader);
		annotationScanFactory.scan(classes);
		this.autowired();
	}
	/**
	 * 刷新容器上下文
	 */
	public void refreshContext(){
		ApplicationContextFactory.setApplicationContext(this);
	}
}
