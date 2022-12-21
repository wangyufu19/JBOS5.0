package com.jbosframework.context.support;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.ConfigurableListableBeanFactory;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AnnotationBeanClassParser;
import com.jbosframework.core.annotaion.AnnotationAwareOrderComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.List;

public class AnnotationBeanClassDelegate {
    private static final Log logger= LogFactory.getLog(AnnotationBeanClassDelegate.class);

    public static void parse(ConfigurableApplicationContext applicationContext,
                             ConfigurableListableBeanFactory beanFactory){
        List<BeanDefinition> beanCandidates = new ArrayList();
        List<String> candidateNames = beanFactory.getBeanDefinitionNames();
        if(candidateNames==null){
            return;
        }
        for(String beanName:candidateNames){
            BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
            beanCandidates.add(beanDef);
        }
        AnnotationBeanClassParser parser=new AnnotationBeanClassParser(applicationContext,beanFactory);
        parser.parse(beanCandidates);
    }

    public static void registerBeanPostProcessor(ConfigurableListableBeanFactory beanFactory){
        String[] beanNames=beanFactory.getBeanNamesOfType(BeanPostProcessor.class);
        if(beanNames==null){
            return;
        }
        List<BeanPostProcessor> list = new ArrayList<BeanPostProcessor>();
        for(String beanName:beanNames){
            BeanPostProcessor beanPostProcessor=(BeanPostProcessor)beanFactory.getBean(beanName);;
            list.add(beanPostProcessor);
        }
        for(BeanPostProcessor beanPostProcessor:beanFactory.getBeanPostProcessors()){
            list.add(beanPostProcessor);
        }
        list.sort(AnnotationAwareOrderComparator.INSTANCE);
        for(int i=0;i<list.size();i++){
            beanFactory.registerBeanPostProcessor(list.get(i));
        }
    }
    public static void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory){
        beanFactory.initialization();
    }
}
