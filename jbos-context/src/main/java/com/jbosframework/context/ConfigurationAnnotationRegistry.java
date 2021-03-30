package com.jbosframework.context;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.context.annotation.ConfigurationAnnotationImport;
import com.jbosframework.utils.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
        annotationBean.setRootBean(true);
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
    /**
     * 注册方法Bean
     * @param cls
     * @param parent
     */
    public void doRegistryMethodBean(Class<?> cls,AnnotationBean parent){
        Method[] methods=cls.getMethods();
        if(methods==null) {
            return;
        }
        for(int i=0;i<methods.length;i++) {
            Annotation[] annotations = methods[i].getDeclaredAnnotations();
            if (annotations == null||annotations.length<=0) {
                continue;
            }
            for(Annotation annotation:annotations){
                AnnotationBean annotationBean=new AnnotationBean();
                String id="";
                annotationBean.setAnnotations(annotations);
                annotationBean.setParentName(parent.getName());
                annotationBean.setIsMethodBean(true);
                if(annotation instanceof Bean){
                    //加载Bean注解
                    id=((Bean)annotation).value();
                    if(StringUtils.isNUll(id)) {
                        id=methods[i].getReturnType().getName();
                    }
                    annotationBean.setInitMethod(((Bean)annotation).initMethod());
                    if(methods[i].getReturnType().isInterface()){
                        this.getBeanFactory().putBeanNameOfType(methods[i].getReturnType().getName(),annotationBean);
                    }
                }
                annotationBean.setId(id);
                annotationBean.setName(id);
                annotationBean.setClassName(methods[i].getReturnType().getName());
                Scope scope=methods[i].getDeclaredAnnotation(Scope.class);
                if(scope!=null){
                    annotationBean.setScope(scope.value());
                }
                annotationBean.setMethodMetadata(MethodMetadata.createMethodMetadata(methods[i]));
                this.getBeanFactory().putBeanDefinition(annotationBean);
            }
        }
    }
}
