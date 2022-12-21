package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.boot.context.ConfigurationProperties;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextAware;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
/**
 * DataSourcePropertyBeanPostProcessor
 * @author youfu.wang
 */
public class DataSourcePropertiesBeanPostProcessor implements ApplicationContextAware, BeanPostProcessor, Ordered {
    private int order=Ordered.HIGHEST_PRECEDENCE+10;
    private ConfigurableApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=(ConfigurableApplicationContext)applicationContext;
    }
    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition){
        if(this.applicationContext.getBeanFactory().containsSingletonBean(beanDefinition.getClassName())){
            return bean;
        }
        GenericBeanDefinition genericBeanDefinition=(GenericBeanDefinition)beanDefinition;
        if(genericBeanDefinition.getRole()==BeanDefinition.ROLE_MEMBER_METHOD){
            if(genericBeanDefinition.getMethodMetadata().isAnnotation(ConfigurationProperties.class)){
                ConfigurationProperties configurationProperties=genericBeanDefinition.getMethodMetadata().getMethod().getDeclaredAnnotation(ConfigurationProperties.class);
                this.doCreateDatasourceProperties(configurationProperties);
            }
        }else{
            if(genericBeanDefinition.getMetadata().isAnnotation(ConfigurationProperties.class)){
                ConfigurationProperties configurationProperties=genericBeanDefinition.getBeanClass().getDeclaredAnnotation(ConfigurationProperties.class);
                this.doCreateDatasourceProperties(configurationProperties);
            }
        }
        return bean;
    }
    private void doCreateDatasourceProperties(ConfigurationProperties configurationProperties){
        DataSourceProperties dataSourceProperties=DataSourcePropertiesBuilder.getInstance().create(this.applicationContext,configurationProperties);
        GenericBeanDefinition genericBeanDefinition=new GenericBeanDefinition(dataSourceProperties.getClass());
        this.applicationContext.getBeanFactory().putBeanDefinition(dataSourceProperties.getClass().getName(),genericBeanDefinition);
        this.applicationContext.getBeanFactory().registerSingletonInstance(dataSourceProperties.getClass().getName(),dataSourceProperties);
    }
    public int getOrder(){
        return this.order;
    }
}
