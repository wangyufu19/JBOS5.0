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
                }  catch (Throwable ex) {
                    throw new IllegalArgumentException("Cannot instantiate " + importClass.getName(), ex);
                }
            }else{
                this.parse(new ConfigurationClass(beanDef.getBeanClass(),beanDef));
            }
        }
    }
    private void processMemberClass(ConfigurationClass configurationClass){
        if(configurationClass.getMetadata().findAnnotation(Configuration.class)&&this.checkCandidateBean(configurationClass)){
            GenericBeanDefinition configurationBeanDef=configurationClass.getBeanDefinition();
            configurationBeanDef.setRole(GenericBeanDefinition.ROLE_COMPONENT_CLASS);
            this.registry.putBeanDefinition(configurationBeanDef.getName(),configurationBeanDef);
            Class<?>[] classes=configurationClass.getSource().getDeclaredClasses();
            if(!ObjectUtils.isEmpty(classes)){
                for(Class<?> cls:classes){
                    GenericBeanDefinition beanDef=new GenericBeanDefinition(cls);
                    beanDef.setRole(GenericBeanDefinition.ROLE_MEMBER_CLASS);
                    beanDef.setParent(configurationBeanDef);
                    ConfigurationClass subConfigurationClass=new ConfigurationClass(beanDef.getBeanClass(),beanDef);
                    if(subConfigurationClass.getMetadata().findAnnotation(Configuration.class)&&this.checkCandidateBean(subConfigurationClass)){
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
                        if(methodMetadata.findAnnotation(Bean.class)&&this.checkCandidateBean(methodMetadata.getMethodAnnotations())){
                            this.registry.putBeanDefinition(methodBeanDef.getName(), methodBeanDef);
                        }
                    }
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

}
