package com.jbosframework.context.annotation;
import com.jbosframework.beans.support.*;
import com.jbosframework.core.env.ConfigurableEnvironment;
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
public class ClassPathBeanDefinitionScanner {
	private static final Log log= LogFactory.getLog(ClassPathBeanDefinitionScanner.class);
	private ConfigurableEnvironment environment;
	private BeanDefinitionRegistry registry;
	private AnnotationFilter annotationFilter=new AnnotationFilter();
	/**
	 * 构造方法
	 */
	public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		this.registry=registry;
	}
	public void setEnvironment(ConfigurableEnvironment configurableEnvironment) {
		this.environment=environment;
	}
	/**
	 * 扫描注解Bean
	 * @param basePackages
	 */
	public void scan(String... basePackages) {
		if(basePackages==null) {
			return;
		}
		for(String basePackage:basePackages){
			this.scan(basePackage);
		}
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
	 * @param basePackage
	 */
	private void scan(String basePackage) {
		if(basePackage==null||"".equals(basePackage)) {
			return;
		}
		List<String> allClasses=AnnotationClassSupport.getBasePackageClasses(ClassLoader.getSystemResource(""),basePackage);
		if(allClasses==null) {
			return;
		}
//		beanReader.setAnnotationFilter(annotationFilter);
		for(int i=0;i<allClasses.size();i++) {
			try {
				Class beanCls=JBOSClassloader.loadClass(allClasses.get(i));
				//判断类型是否为接口，如果不是接口则就加载Bean定义对象
				if(!beanCls.isInterface()){
					//加载类的Bean定义对象
//					this.beanReader.loadBeanDefinition(beanCls);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}