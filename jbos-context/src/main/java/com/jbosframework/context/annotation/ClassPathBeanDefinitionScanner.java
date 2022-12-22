package com.jbosframework.context.annotation;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.support.*;
import com.jbosframework.core.env.ConfigurableEnvironment;
import java.util.LinkedHashSet;
import java.util.Set;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ClassPathBeanDefinitionScanner
 * @author youfu.wang
 * @version 1.0
 */
public class ClassPathBeanDefinitionScanner extends AnnotationComponentScanProvider{
	private static final Log logger= LogFactory.getLog(ClassPathBeanDefinitionScanner.class);
	private ConfigurableEnvironment environment;
	private BeanDefinitionRegistry registry;


	public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		this.registry=registry;
	}
	public void setEnvironment(ConfigurableEnvironment configurableEnvironment) {
		this.environment=environment;
	}

	public Set<BeanDefinition> scan(String... basePackages) {
		Set<BeanDefinition> beanDefinitions = new LinkedHashSet();
		if(basePackages==null) {
			return beanDefinitions;
		}
		for(String basePackage:basePackages){
			this.doScan(basePackage,beanDefinitions);
		}
		return beanDefinitions;
	}

	private void doScan(String basePackage,Set<BeanDefinition> beanDefinitions){
		if(StringUtils.isNUll(basePackage)){
			return;
		}
		Set<GenericBeanDefinition> candidates=this.findCandidateComponents(basePackage);
		if(candidates==null) {
			return;
		}
		GenericBeanDefinition[] arr=candidates.toArray(new GenericBeanDefinition[0]);
		for(GenericBeanDefinition beanDefinition:arr){
			beanDefinitions.add(beanDefinition);
		}
	}
}