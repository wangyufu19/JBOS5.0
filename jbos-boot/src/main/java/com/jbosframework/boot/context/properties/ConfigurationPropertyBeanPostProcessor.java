package com.jbosframework.boot.context.properties;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.boot.context.EnableConfigurationProperties;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
import com.jbosframework.utils.JBOSClassloader;
import com.jbosframework.utils.StringUtils;
import com.jbosframework.utils.TypeConverter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * ConfigurationPropertyBeanPostProcessor
 * @author youfu.wang
 */
public class ConfigurationPropertyBeanPostProcessor implements BeanPostProcessor, Ordered {
    private int order=Ordered.HIGHEST_PRECEDENCE+1;
    private ConfigurableApplicationContext applicationContext;

    public ConfigurationPropertyBeanPostProcessor(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition){
        GenericBeanDefinition genericBeanDefinition=(GenericBeanDefinition)beanDefinition;
        if(genericBeanDefinition.getMetadata().isAnnotation(EnableConfigurationProperties.class)){
            EnableConfigurationProperties enableConfigurationProperties=(EnableConfigurationProperties)genericBeanDefinition.getMetadata().findAnnotation(EnableConfigurationProperties.class);
            Class[] configurationPropertiesClasses=enableConfigurationProperties.value();
            for(Class configurationPropertiesClass:configurationPropertiesClasses){
                Object configurationPropertiesObject=JBOSClassloader.newInstance(configurationPropertiesClass);
                ConfigurationProperties configurationProperties=(ConfigurationProperties)configurationPropertiesClass.getDeclaredAnnotation(ConfigurationProperties.class);
                String prefix=configurationProperties.prefix();

                Field[] fields=configurationPropertiesObject.getClass().getSuperclass().getDeclaredFields();
                loadProperties(configurationPropertiesObject,fields,prefix);
                fields=configurationPropertiesObject.getClass().getDeclaredFields();
                loadProperties(configurationPropertiesObject,fields,prefix);
            }
        }
        return bean;
    }
    private void loadProperties(Object configurationPropertiesObject, Field[] fields, String prefix){
        for(Field field:fields){
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                this.loadProperties(configurationPropertiesObject,field,prefix);
            }
        }
    }
    private void loadProperties(Object configurationPropertiesObject,Field field,String prefix){
        if(prefix.indexOf(".")!=-1){
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                Object value= TypeConverter.convert(field.getType().getName(),applicationContext.getEnvironment().getProperty(prefix+"."+field.getName()));
                if(StringUtils.isNotNUll(value)){
                    InjectionMetadata.inject(configurationPropertiesObject,field,value);
                }
            }
            prefix=prefix.substring(0,prefix.lastIndexOf("."));
            this.loadProperties(configurationPropertiesObject,field,prefix);
        }
    }
    public int getOrder(){
        return this.order;
    }
}
