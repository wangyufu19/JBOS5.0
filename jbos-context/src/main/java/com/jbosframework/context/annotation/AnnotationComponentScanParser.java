package com.jbosframework.context.annotation;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.annotaion.AnnotationUtils;
import com.jbosframework.core.env.ConfigurableEnvironment;
import java.util.LinkedHashSet;
import java.util.Set;

public class AnnotationComponentScanParser {
    private final ClassPathBeanDefinitionScanner scanner;
    private ConfigurableApplicationContext applicationContext;
    private BeanDefinitionRegistry registry;


    public AnnotationComponentScanParser(ConfigurableApplicationContext applicationContext,
                                     BeanDefinitionRegistry registry) {
        this.applicationContext = applicationContext;
        this.registry = registry;
        this.scanner=new ClassPathBeanDefinitionScanner(registry);
        for(Class filter:IncludeFilter.DEFAULT_FILTERS){
            this.scanner.addIncludeFilters(filter);
        }
    }
    public Set<BeanDefinition> parse(Class configurationClass,Class annotationType){
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet();
        if (AnnotationUtils.isComponent(configurationClass,annotationType)){
            ComponentScan componentScan=(ComponentScan)AnnotationUtils.findAnnotation(configurationClass,annotationType);
            String[] basePackages=new String[1];
            if(componentScan.basePackages()==null||componentScan.basePackages().length<=0){
                basePackages[0]=configurationClass.getPackage().getName();
                beanDefinitions=scanner.scan(basePackages);
            }else{
                beanDefinitions=scanner.scan(componentScan.basePackages());
            }
        }
        return beanDefinitions;
    }

    public ClassPathBeanDefinitionScanner getScanner() {
        return scanner;
    }
}
