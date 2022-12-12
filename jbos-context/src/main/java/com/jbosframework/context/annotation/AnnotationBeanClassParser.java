package com.jbosframework.context.annotation;

import com.jbosframework.beans.config.AnnotationMetadata;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.core.annotaion.AnnotationUtils;
import com.jbosframework.core.env.ConfigurableEnvironment;
import com.jbosframework.utils.JBOSClassCaller;
import com.jbosframework.utils.JBOSClassloader;
import com.jbosframework.utils.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
    }
    private void doProcessConfigurationClass(ConfigurationClass configurationClass){
        if (AnnotationUtils.isComponent(configurationClass.source,ComponentScan.class)){
            Set<BeanDefinition> beanCandidates=this.componentScanParser.parse(configurationClass.source,ComponentScan.class);
            Iterator<BeanDefinition> beanDefinitions=beanCandidates.iterator();
            while(beanDefinitions.hasNext()){
                GenericBeanDefinition beanDef=(GenericBeanDefinition)beanDefinitions.next();
                configurationClass=new ConfigurationClass(beanDef.getBeanClass(),beanDef.getMetadata());
                if(this.checkCandidateBean(configurationClass)){
                    this.registry.putBeanDefinition(beanDef.getName(),beanDef);
                    this.parse(configurationClass);
                }
            }
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
                this.parse(new ConfigurationClass(beanDef.getBeanClass(),beanDef.getMetadata()));
            }
        }
    }
    private void processMemberClass(ConfigurationClass configurationClass){
        GenericBeanDefinition configurationBeanDef=new GenericBeanDefinition(configurationClass.getSource());
        if(AnnotationUtils.isComponent(configurationClass.getSource(),Configuration.class)&&this.checkCandidateBean(configurationClass)){
            this.registry.putBeanDefinition(configurationBeanDef.getName(),configurationBeanDef);
            Class<?>[] classes=configurationClass.getSource().getDeclaredClasses();
            if(!ObjectUtils.isEmpty(classes)){
                for(Class<?> cls:classes){
                    GenericBeanDefinition beanDef=new GenericBeanDefinition(cls);
                    this.parse(new ConfigurationClass(beanDef.getBeanClass(),beanDef.getMetadata()));
                }
            }
            Method[] methods=configurationClass.getSource().getDeclaredMethods();
            if(!ObjectUtils.isEmpty(methods)){
                for(Method method:methods){
                    MethodMetadata methodMetadata=MethodMetadata.createMethodMetadata(method);
                    if(methodMetadata.isPublic()) {
                        GenericBeanDefinition methodBeanDef = new GenericBeanDefinition(method.getReturnType());
                        methodBeanDef.setParent(configurationBeanDef);
                        methodBeanDef.setMethodMetadata(methodMetadata);
                        this.registry.putBeanDefinition(methodBeanDef.getName(), methodBeanDef);
                        logger.info(method.getReturnType().getName() + ":modifier=" + method.getModifiers());
                    }
                }
            }
        }
    }
    private boolean checkCandidateBean(ConfigurationClass configurationClass){
        boolean check=true;
        Annotation[] annotations=configurationClass.getMetadata().getAnnotations();
        for(Annotation annotation:annotations){
            Conditional conditional=AnnotationUtils.findAnnotation(annotation.annotationType(),Conditional.class);
            if(conditional!=null){ ;
                Class<?>[] parameterTypes={ConfigurableEnvironment.class,BeanDefinitionRegistry.class, Annotation.class};
                Object[] args={this.environment,this.registry,annotation};
                Condition condition=(Condition) JBOSClassloader.newInstance(conditional.value(),parameterTypes,args);
                if(!condition.matches()){
                    check=false;
                    break;
                }
            }
        }
        return check;
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
