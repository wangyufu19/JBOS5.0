package com.jbosframework.context.support;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.factory.ConfigurableListableBeanFactory;
import com.jbosframework.context.annotation.AnnotationBeanClassParser;
import com.jbosframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.List;

public class AnnotationBeanClassDelegate {


    public static void parse(ConfigurableEnvironment environment,
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
        AnnotationBeanClassParser parser=new AnnotationBeanClassParser(environment,beanFactory);
        parser.parse(beanCandidates);
    }
}
