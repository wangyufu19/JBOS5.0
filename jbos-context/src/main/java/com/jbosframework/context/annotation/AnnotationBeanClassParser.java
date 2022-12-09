package com.jbosframework.context.annotation;

import com.jbosframework.beans.config.AnnotationMetadata;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.core.annotaion.AnnotationUtils;
import com.jbosframework.core.env.ConfigurableEnvironment;
import com.jbosframework.utils.JBOSClassCaller;
import com.jbosframework.utils.JBOSClassloader;
import com.jbosframework.utils.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AnnotationBeanClassParser {
    public static final Log logger= LogFactory.getLog(AnnotationBeanClassParser.class);

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
            this.parse(configurationClass);
        }
    }
    private void parse(ConfigurationClass configurationClass){
        this.doProcessConfigurationClass(configurationClass);
        this.processImports(configurationClass);
        this.processMemberClass(configurationClass);
        logger.info(configurationClass.getSource().getName());
    }
    private void doProcessConfigurationClass(ConfigurationClass configurationClass){
        Set<BeanDefinition> beanCandidates=this.componentScanParser.parse(configurationClass.source,ComponentScan.class);
        Iterator<BeanDefinition> beanDefinitions=beanCandidates.iterator();
        while(beanDefinitions.hasNext()){
            BeanDefinition beanDefinition=beanDefinitions.next();
            GenericBeanDefinition beanDef=(GenericBeanDefinition)beanDefinition;
            configurationClass=new ConfigurationClass(beanDef.getBeanClass(),beanDef.getMetadata());
            this.parse(configurationClass);
        }
    }
    private void processImports(ConfigurationClass configurationClass){
        Import importAnnotation=(Import) AnnotationUtils.findAnnotation(configurationClass.getSource(),Import.class);
        if(importAnnotation==null){
            return;
        }
        Class[] importClasses=importAnnotation.value();
        for(Class importClass:importClasses){
            GenericBeanDefinition beanDef=new GenericBeanDefinition(importClass);
            if(ImportSelector.class.isAssignableFrom(importClass)){
                try {
                    Class<?>[] parameterTypes={this.registry.getClass()};
                    Object[] args={this.registry};
                    Constructor<?> constructor = importClass.getDeclaredConstructor(parameterTypes);
                    Object instance = constructor.newInstance(args);
                    List<String> beanCandidates=(List<String>)JBOSClassCaller.call(instance,"processImports");
                    if(!ObjectUtils.isEmpty(beanCandidates)){
                        for(String beanCandidate:beanCandidates){
                            Class CandidateClass=JBOSClassloader.loadClass(beanCandidate);
                            beanDef=new GenericBeanDefinition(CandidateClass);
                            this.parse(new ConfigurationClass(beanDef.getBeanClass(),beanDef.getMetadata()));
                        }
                    }
                }  catch (Throwable ex) {
                    throw new IllegalArgumentException("Cannot instantiate " + importClass.getName(), ex);
                }
            }else{
                if(AnnotationUtils.isComponent(importClass,Configuration.class)){
                    this.parse(new ConfigurationClass(beanDef.getBeanClass(),beanDef.getMetadata()));
                }
            }
        }
    }
    private void processMemberClass(ConfigurationClass configurationClass){
        Class<?>[] classes=configurationClass.getSource().getDeclaredClasses();
        if(!ObjectUtils.isEmpty(classes)){
            for(Class<?> cls:classes){
                GenericBeanDefinition beanDef=new GenericBeanDefinition(cls);
                this.parse(new ConfigurationClass(beanDef.getBeanClass(),beanDef.getMetadata()));
            }
        }
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

        public AnnotationMetadata getMetadata() {
            return metadata;
        }
    }

}
