package com.jbosframework.beans.factory;
import com.jbosframework.beans.support.BeanReader;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.annotation.*;
import com.jbosframework.core.JBOSClassloader;
import com.jbosframework.beans.support.AnnotationBeanReaderImpl;
import java.util.List;
import com.jbosframework.beans.annotation.AnnotationFilter;
import com.jbosframework.beans.support.AnnotationClassSupport;
/**
 * AnnotationScanFactory
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationScanFactory {
	private ApplicationContext ctx;
	private BeanReader beanReader;
	private AnnotationFilter annotationFilter=new AnnotationFilter();

	/**
	 * 构造方法
	 */
	public AnnotationScanFactory(ApplicationContext ctx) {
		this.ctx=ctx;
		beanReader=new AnnotationBeanReaderImpl(ctx);
	}
	/**
	 * 扫描注解Bean
	 * @param cls
	 */
	public void scan(Class<?> cls) {		
		if(cls==null) {
			return;
		}
		Configuration configuration=cls.getAnnotation(Configuration.class);
		if(configuration==null) {
			return;
		}
		//切面自动代理
		EnableAspectJAutoProxy enableAspectJAutoProxy=cls.getAnnotation(EnableAspectJAutoProxy.class);
		if(enableAspectJAutoProxy!=null){
			this.ctx.getContextConfiguration().setEnableAspectJAutoProxy(enableAspectJAutoProxy.proxyTargetClass());
		}
		//扫描注解Bean
		this.scanComponent(cls);
	}
	/**
	 * 扫描注解Bean
	 * @param cls
	 */
	private void scanComponent(Class<?> cls){
		String[] basePackages=null;
		AutoConfiguration autoConfiguration=cls.getAnnotation(AutoConfiguration.class);
		if(autoConfiguration!=null){
			basePackages=new String[1];
			basePackages[0]=cls.getPackage().getName();
		}else{
			ComponentScan componentScan=cls.getAnnotation(ComponentScan.class);
			if(componentScan!=null) {
				basePackages=componentScan.basePackages();
				Class<?>[] include=componentScan.include();
				annotationFilter.setInclude(include);
			}
		}
		if(basePackages==null){
			return;
		}
		for(int i=0;i<basePackages.length;i++) {
			this.scan(basePackages[i]);
		}
	}
	/**
	 * 扫描注解Bean
	 * @param clses
	 */
	public void scan(Class<?>[] clses) {
		if(clses==null) {
			return;
		}
		for(int i=0;i<clses.length;i++) {
			this.scan(clses[i]);
		}
	}
	/**
	 * 扫描注解Bean
	 * @param basePackages
	 */
	public void scan(String basePackages) {
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
				this.beanReader.loadBeanDefinition(JBOSClassloader.loadClass(allClasses.get(i)));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
