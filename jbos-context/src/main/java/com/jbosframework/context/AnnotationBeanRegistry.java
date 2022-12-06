package com.jbosframework.context;
import com.jbosframework.beans.annotation.*;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.support.AbstractBeanRegistry;
import com.jbosframework.beans.support.ConfigurableBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.annotation.Annotation;
/**
 * AnnotationBeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationBeanRegistry extends AbstractBeanRegistry {

    private static final Log log= LogFactory.getLog(AnnotationBeanRegistry.class);
    private ConfigurableBeanFactory registry;
    /**
     * 构造方法
     * @param registry
     */
    public AnnotationBeanRegistry(ConfigurableBeanFactory registry){
        this.registry=registry;
    }

    public enum AnnotationType{
        COMPONENT,
        BEAN,
        SERVICE,
        REPOSITORY;

        private AnnotationType(){

        }
    }
    /**
     * 注册Bean
     * @param cls
     * @param beanId
     */
    private void registry(Class<?> cls,String beanId){
        GenericBeanDefinition annotationBean=new GenericBeanDefinition(cls);
        this.registry.putBeanDefinition(annotationBean.getName(),annotationBean);
        //注册Bean的接口
        this.registryBeanInterfaces(cls,annotationBean);
    }
    //注册Bean的接口
    private void registryBeanInterfaces(Class<?> cls, GenericBeanDefinition annotationBean){
        Class<?>[] interfaces=cls.getInterfaces();
        if(interfaces==null){
            return;
        }
        for(Class interfaceCls:interfaces){
            this.registry.putBeanNameOfType(interfaceCls.getName(),annotationBean);
        }
    }
    /**
     * 注册Bean
     * @param cls
     */
    public void registry(Class<?> cls){
        if(cls==null||cls.isAnnotation()){
            return;
        }
        Annotation[] annotations=cls.getDeclaredAnnotations();
        if(annotations==null) {
            return;
        }
        //注册Component注解
        Component component=cls.getAnnotation(Component.class);
        if(component!=null){
            this.registry(cls,component.value());
        }
        //注册Bean注解
        Bean bean=cls.getAnnotation(Bean.class);
        if(bean!=null){
            this.registry(cls,bean.value());
        }
        //注册Service注解
        Service service=cls.getAnnotation(Service.class);
        if(service!=null){
            this.registry(cls,service.value());
        }
        //注册Repository注解
        Repository repository=cls.getAnnotation(Repository.class);
        if(repository!=null){
            this.registry(cls,repository.value());
        }
    }
}
