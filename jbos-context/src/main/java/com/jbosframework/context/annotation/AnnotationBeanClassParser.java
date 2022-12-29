package com.jbosframework.context.annotation;

import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.config.AnnotationMetadata;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.annotaion.AnnotationUtils;
import com.jbosframework.utils.JBOSClassCaller;
import com.jbosframework.utils.JBOSClassloader;
import com.jbosframework.utils.ObjectUtils;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AnnotationBeanClassParser {
    public static final Log logger= LogFactory.getLog(AnnotationBeanClassParser.class);

    private ConfigurableApplicationContext applicationContext;
    private BeanDefinitionRegistry registry;
    private AnnotationComponentScanParser componentScanParser;

    public AnnotationBeanClassParser(ConfigurableApplicationContext applicationContext,
                                     BeanDefinitionRegistry registry){
        this.applicationContext=applicationContext;
        this.registry=registry;
        this.componentScanParser=new AnnotationComponentScanParser(this.applicationContext,registry);
    }
    public void parse(List<BeanDefinition> beanCandidates) {
        if(beanCandidates==null){
            return;
        }
        for(BeanDefinition beanCandidate:beanCandidates){
            GenericBeanDefinition beanDef=(GenericBeanDefinition)beanCandidate;
            ConfigurationClass configurationClass=new ConfigurationClass(beanDef.getBeanClass(),beanDef);
            this.parse(configurationClass);
        }
    }
    private void parse(ConfigurationClass configurationClass){
        this.doProcessConfigurationClass(configurationClass);
        this.processMemberClass(configurationClass);
        this.processImports(configurationClass);
    }
    private void doProcessConfigurationClass(ConfigurationClass configurationClass){
        if (AnnotationUtils.isComponent(configurationClass.source,ComponentScan.class)){
            Set<BeanDefinition> beanCandidates=this.componentScanParser.parse(configurationClass.source,ComponentScan.class);
            Iterator<BeanDefinition> beanDefinitions=beanCandidates.iterator();
            while(beanDefinitions.hasNext()){
                GenericBeanDefinition beanDef=(GenericBeanDefinition)beanDefinitions.next();
                configurationClass=new ConfigurationClass(beanDef.getBeanClass(),beanDef);
                if(this.checkCandidateBean(configurationClass)){
                    this.registryBeanDefinition(beanDef);
                    this.parse(configurationClass);
                }
            }
        }
    }
    private void processMemberClass(ConfigurationClass configurationClass){
        if(AnnotationUtils.isComponent(configurationClass.source,Configuration.class)&&this.checkCandidateBean(configurationClass)){
            GenericBeanDefinition configurationBeanDef=configurationClass.getBeanDefinition();
            configurationBeanDef.setRole(GenericBeanDefinition.ROLE_COMPONENT_CLASS);
            this.registryBeanDefinition(configurationBeanDef);

            Class<?>[] classes=configurationClass.getSource().getDeclaredClasses();
            if(!ObjectUtils.isEmpty(classes)){
                for(Class<?> cls:classes){
                    GenericBeanDefinition beanDef=new GenericBeanDefinition(cls);
                    beanDef.setRole(GenericBeanDefinition.ROLE_MEMBER_CLASS);
                    beanDef.setParent(configurationBeanDef);
                    ConfigurationClass subConfigurationClass=new ConfigurationClass(beanDef.getBeanClass(),beanDef);
                    if(subConfigurationClass.getMetadata().isAnnotation(Configuration.class)&&this.checkCandidateBean(subConfigurationClass)){
                        this.parse(subConfigurationClass);
                    }
                }
            }

            Method[] methods=configurationClass.getSource().getDeclaredMethods();
            if(!ObjectUtils.isEmpty(methods)){
                for(Method method:methods){
                    MethodMetadata methodMetadata=MethodMetadata.createMethodMetadata(method);
                    if(methodMetadata.isPublic()) {
                        GenericBeanDefinition methodBeanDef = new GenericBeanDefinition(method.getReturnType());
                        methodBeanDef.setRole(GenericBeanDefinition.ROLE_MEMBER_METHOD);
                        methodBeanDef.setParent(configurationBeanDef);
                        methodBeanDef.setMethodMetadata(methodMetadata);
                        if(methodMetadata.isAnnotation(Bean.class)&&this.checkCandidateBean(methodMetadata.getMethodAnnotations())){
                            this.registryBeanDefinition(methodBeanDef);
                        }
                    }
                }
            }
        }
    }
    private void processImports(ConfigurationClass configurationClass){
        Annotation[] importAnnotations=AnnotationUtils.findAnnotations(configurationClass.getSource(),Import.class);
        if(importAnnotations==null||importAnnotations.length<=0){
            return;
        }
        for(Annotation importAnnotation:importAnnotations){
            Class[] importClasses=((Import)importAnnotation).value();
            for(Class importClass:importClasses){
                GenericBeanDefinition beanDef=new GenericBeanDefinition(importClass);
                try {
                    if(ImportSelector.class.isAssignableFrom(importClass)){
                        Class<?>[] parameterTypes={ConfigurableApplicationContext.class};
                        Object[] args={this.applicationContext};
                        Constructor<?> constructor = importClass.getDeclaredConstructor(parameterTypes);
                        Object instance = constructor.newInstance(args);
                        List<String> beanCandidates=(List<String>)JBOSClassCaller.call(instance,"processImports");
                        if(!ObjectUtils.isEmpty(beanCandidates)){
                            for(String beanCandidate:beanCandidates){
                                Class CandidateClass=JBOSClassloader.loadClass(beanCandidate);
                                beanDef=new GenericBeanDefinition(CandidateClass);
                                this.parse(new ConfigurationClass(beanDef.getBeanClass(),beanDef));
                            }
                        }
                    }else if(ImportBeanDefinitionSelector.class.isAssignableFrom(importClass)){
                        Class<?>[] parameterTypes={ConfigurableApplicationContext.class};
                        Object[] args={this.applicationContext};
                        Constructor<?> constructor = importClass.getDeclaredConstructor(parameterTypes);
                        Object instance = constructor.newInstance(args);
                        Object[] methodArgs={configurationClass};
                        JBOSClassCaller.call(instance,"processImport",methodArgs);
                    }else{
                        this.parse(new ConfigurationClass(beanDef.getBeanClass(),beanDef));
                    }
                }  catch (Throwable ex) {
                    throw new IllegalArgumentException("Cannot instantiate " + importClass.getName(), ex);
                }
            }
        }
    }
    private boolean checkCandidateBean(ConfigurationClass configurationClass){
        return checkCandidateBean(configurationClass.getMetadata().getAnnotations());
    }
    private boolean checkCandidateBean(Annotation[] annotations){
        boolean check=true;
        for(Annotation annotation:annotations){
            Conditional conditional=AnnotationUtils.findAnnotation(annotation.annotationType(),Conditional.class);
            if(conditional!=null){
                Class<?>[] parameterTypes={ConfigurableApplicationContext.class, Annotation.class};
                Object[] args={this.applicationContext,annotation};
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
        private GenericBeanDefinition beanDefinition;

        public ConfigurationClass(Class source,GenericBeanDefinition beanDefinition){
            this.source=source;
            this.beanDefinition=beanDefinition;
        }

        public Class getSource() {
            return source;
        }

        public GenericBeanDefinition getBeanDefinition() {
            return beanDefinition;
        }

        public AnnotationMetadata getMetadata() {
            if(this.beanDefinition!=null){
                return beanDefinition.getMetadata();
            }else{
                return new AnnotationMetadata();
            }
        }
    }
    private void registryBeanDefinition(GenericBeanDefinition genericBeanDefinition){
        Bean bean;
        if(genericBeanDefinition.getRole()==GenericBeanDefinition.ROLE_MEMBER_METHOD){
            MethodMetadata methodMetadata=genericBeanDefinition.getMethodMetadata();
            bean=(Bean) methodMetadata.findAnnotation(Bean.class);
        }else{
            bean=(Bean) genericBeanDefinition.getMetadata().findAnnotation(Bean.class);
        }
        if(StringUtils.isNotNUll(bean)&&StringUtils.isNotNUll(bean.value())){
            genericBeanDefinition.setId(bean.value());
            genericBeanDefinition.setName(bean.value());
        }
        this.registry.putBeanDefinition(genericBeanDefinition.getName(),genericBeanDefinition);
    }
}
