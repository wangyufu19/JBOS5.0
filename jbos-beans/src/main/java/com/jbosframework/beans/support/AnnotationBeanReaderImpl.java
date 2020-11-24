package com.jbosframework.beans.support;
import com.jbosframework.beans.annotation.AnnotationFilter;
import com.jbosframework.beans.factory.BeanFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * AnnotationBeanReaderImpl
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanReaderImpl implements BeanReader {
	private BeanFactory beanFactory;
	private List<BeanRegistry> beanRegistries=new ArrayList<BeanRegistry>();
	private AnnotationFilter annotationFilter;
	
	/**
	 * 构造方法
	 * @param beanFactory
	 */
	public AnnotationBeanReaderImpl(BeanFactory beanFactory) {
		this.beanFactory=beanFactory;
	}
	/**
	 * 添加BeanRegistry
	 * @param beanRegistry
	 */
	public void addBeanRegistry(BeanRegistry beanRegistry){
		beanRegistries.add(beanRegistry);
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
		for (BeanRegistry beanRegistry:beanRegistries){
			//注册Bean到上下文
			beanRegistry.registerBean(cls);
		}
	}
}
