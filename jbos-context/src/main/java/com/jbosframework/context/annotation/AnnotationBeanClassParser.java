package com.jbosframework.context.annotation;

import com.jbosframework.beans.config.AnnotationMetadata;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.core.annotaion.AnnotationUtils;
import com.jbosframework.core.env.ConfigurableEnvironment;
import java.util.List;
import java.util.Set;

public class AnnotationBeanClassParser {
    private ConfigurableEnvironment environment;
    private BeanDefinitionRegistry registry;
    private AnnotationComponentScanParser componentScanParser;

    public AnnotationBeanClassParser(ConfigurableEnvironment environment,
                                     BeanDefinitionRegistry registry){
        this.environment=environment;
        this.registry=registry;
        this.componentScanParser=new AnnotationComponentScanParser(environment,registry);
    }
    public void parse(List<BeanDefinition> beanCandidates) {
        if(beanCandidates==null){
            return;
        }
        for(BeanDefinition beanCandidate:beanCandidates){
            GenericBeanDefinition beanDef=(GenericBeanDefinition)beanCandidate;
            ConfigurationClass configurationClass=new ConfigurationClass(beanDef.getBeanClass(),beanDef.getMetadata());
            this.doProcessConfigurationClass(configurationClass);
        }
    }

    private void doProcessConfigurationClass(ConfigurationClass configurationClass){
        Set<BeanDefinition> beanDefinitions=this.componentScanParser.parse(configurationClass.source,ComponentScan.class);

    }

    public class ConfigurationClass{
        private Class source;
        private AnnotationMetadata metadata;

        public ConfigurationClass(Class source,AnnotationMetadata metadata){
            this.source=source;
            this.metadata=metadata;
        }

        public Class getSource() {
            return source;
        }
    }

}
