package com.jbosframework.context;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.ConfigurationAnnotationImport;

/**
 * ConfigurationAnnotationRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class ConfigurationAnnotationRegistry extends BeanRegistry {
    /**
     * 构造方法
     * @param beanFactory
     */
    public ConfigurationAnnotationRegistry(BeanFactory beanFactory){
        super(beanFactory);
    }

    /**
     * 注册Bean
     * @param cls
     */
    public void registry(Class<?> cls){
        Configuration configuration=cls.getAnnotation(Configuration.class);
        if(configuration==null){
           return;
        }
        AnnotationBean annotationBean=AnnotationBean.createAnnotationBean(configuration.value(),cls);
        annotationBean.setAnnotations(cls.getDeclaredAnnotations());
        this.getBeanFactory().putBeanDefinition(annotationBean);
        //注册方法Bean
        this.doRegistryMethodBean(cls,annotationBean);
        ConfigurationAnnotationImport configurationAnnotationImport=new ConfigurationAnnotationImport(this.getBeanFactory());
        //启用异步注解
        configurationAnnotationImport.doImport(cls,ConfigurationAnnotationImport.AnnotationType.EnableAsync);
        //启用任务注解
        configurationAnnotationImport.doImport(cls,ConfigurationAnnotationImport.AnnotationType.EnableScheduling);
    }

}
