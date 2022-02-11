package com.jbosframework.context;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.support.AbstractBeanRegistry;
import com.jbosframework.beans.support.ConfigurableBeanFactory;
import com.jbosframework.context.annotation.Configuration;
import com.jbosframework.utils.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * ConfigurationAnnotationRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class ConfigurationAnnotationRegistry extends AbstractBeanRegistry {
    private ConfigurableBeanFactory registry;
    /**
     * 构造方法
     * @param registry
     */
    public ConfigurationAnnotationRegistry(ConfigurableBeanFactory registry){
        this.registry=registry;
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
        this.registry.putBeanDefinition(annotationBean.getName(),annotationBean);
        //注册方法Bean
        this.doRegistryMethodBean(cls,annotationBean);
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
            if (annotations == null) {
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
                    annotationBean.setId(id);
                    annotationBean.setName(id);
                    annotationBean.setClassName(methods[i].getReturnType().getName());
                    Scope scope=methods[i].getDeclaredAnnotation(Scope.class);
                    if(scope!=null){
                        annotationBean.setScope(scope.value());
                    }
                    annotationBean.setMethodMetadata(MethodMetadata.createMethodMetadata(methods[i]));
                    this.registry.putBeanDefinition(annotationBean.getName(),annotationBean);
                }
            }
        }
    }
}
