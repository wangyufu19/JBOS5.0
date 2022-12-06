package com.jbosframework.context.annotation;
import com.jbosframework.context.support.AbstractApplicationContext;
import com.jbosframework.core.env.ConfigurableEnvironment;

/**
 * AnnotationConfigApplicationContext
 * @author youfu.wang
 */
public class AnnotationConfigApplicationContext extends AbstractApplicationContext implements AnnotationConfigRegistry{
	private final AnnotatedBeanDefinitionReader reader;
	private final ClassPathBeanDefinitionScanner scanner;

	public AnnotationConfigApplicationContext() {
		this.reader = new AnnotatedBeanDefinitionReader(this);
		this.scanner = new ClassPathBeanDefinitionScanner(this);
	}
	public void setEnvironment(ConfigurableEnvironment environment) {
		super.setEnvironment(environment);
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}

	public void registry(Class<?> cls) {
		this.reader.registryBean(cls);
		this.refresh();
	}

	public void registry(Class<?>[] classes) {
		this.reader.registryBean(classes);
		this.refresh();
	}

	public void scan(String... basePackages){
		scanner.scan(basePackages);
		this.refresh();
	}
}
