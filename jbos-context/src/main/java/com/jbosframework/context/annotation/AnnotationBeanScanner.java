package com.jbosframework.context.annotation;
import com.jbosframework.beans.support.*;
import com.jbosframework.context.AnnotationBeanRegistry;
import com.jbosframework.context.ConfigurationAnnotationRegistry;
import com.jbosframework.utils.JBOSClassloader;
import java.util.List;
import com.jbosframework.beans.annotation.AnnotationFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AnnotationBeanScanner
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanScanner {
	private static final Log log= LogFactory.getLog(AnnotationBeanScanner.class);
	private BeanReader beanReader;
	private AnnotationFilter annotationFilter=new AnnotationFilter();
	/**
	 * 构造方法
	 */
	public AnnotationBeanScanner(ConfigurableBeanFactory registry) {
		this.beanReader=new AnnotationBeanReaderImpl(registry);
		this.beanReader.addBeanRegistry(new AnnotationBeanRegistry(registry));
		this.beanReader.addBeanRegistry(new ConfigurationAnnotationRegistry(registry));
	}
	/**
	 * 添加BeanRegistry
	 * @param beanRegistry
	 */
	public void addBeanRegistry(BeanRegistry beanRegistry){
		this.beanReader.addBeanRegistry(beanRegistry);
	}
	/**
	 * 扫描注解Bean
	 * @param cls
	 */
	public void scan(Class<?> cls) {		
		if(cls==null) {
			return;
		}
		//扫描注解Bean
		this.scanComponent(cls);
	}
	/**
	 * 扫描注解Bean
	 * @param cls
	 */
	private void scanComponent(Class<?> cls){
		String[] basePackages=new String[1];
		basePackages[0]=cls.getPackage().getName();
		ComponentScan componentScan=cls.getAnnotation(ComponentScan.class);
		if(componentScan!=null) {
			basePackages=componentScan.basePackages();
			Class<?>[] include=componentScan.include();
			annotationFilter.setInclude(include);
		}
		for(String basePackage:basePackages) {
			this.scan(basePackage);
		}
	}
	/**
	 * 扫描注解Bean
	 * @param classes
	 */
	public void scan(Class<?>[] classes) {
		if(classes==null) {
			return;
		}
		for(int i=0;i<classes.length;i++) {
			this.scan(classes[i]);
		}
	}
	/**
	 * 扫描注解Bean
	 * @param basePackages
	 */
	private void scan(String basePackages) {
		if(basePackages==null||"".equals(basePackages)) {
			return;
		}
		List<String> allClasses=AnnotationClassSupport.getBasePackageClasses(ClassLoader.getSystemResource(""),basePackages);
		if(allClasses==null) {
			return;
		}
		beanReader.setAnnotationFilter(annotationFilter);
		for(int i=0;i<allClasses.size();i++) {
			try {
				Class beanCls=JBOSClassloader.loadClass(allClasses.get(i));
				//判断类型是否为接口，如果不是接口则就加载Bean定义对象
				if(!beanCls.isInterface()){
					//加载类的Bean定义对象
					this.beanReader.loadBeanDefinition(beanCls);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}