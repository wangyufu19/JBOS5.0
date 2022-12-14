package com.jbosframework.boot.context.properties;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.core.Ordered;

/**
 * ConfigurationPropertyBeanPostProcessor
 * @author youfu.wang
 */
public class ConfigurationPropertyBeanPostProcessor implements BeanPostProcessor, Ordered {
    private int order=Ordered.HIGHEST_PRECEDENCE+10;

    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition){
        GenericBeanDefinition genericBeanDefinition=(GenericBeanDefinition)beanDefinition;
        if(genericBeanDefinition.getRole()==BeanDefinition.ROLE_MEMBER_METHOD){
            if(genericBeanDefinition.getMethodMetadata().findAnnotation(ConfigurationProperties.class)){

            }
        }else{
            if(genericBeanDefinition.getMetadata().findAnnotation(ConfigurationProperties.class)){

            }
        }
        return bean;
    }

    public int getOrder(){
        return this.order;
    }
}
