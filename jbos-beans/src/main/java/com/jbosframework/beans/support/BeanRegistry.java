package com.jbosframework.beans.support;

import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * BeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public abstract class BeanRegistry {
    private BeanFactory beanFactory;

    /**
     * 构造方法
     */
    public BeanRegistry(){

    }
    /**
     * 构造方法
     * @param beanFactory
     */
    public BeanRegistry(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public void setBeanFactory(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public BeanFactory getBeanFactory(){
        return beanFactory;
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
                if(annotation instanceof Bean){
                    //加载Bean注解
                    AnnotationBean annotationBean=new AnnotationBean();
                    String id=((Bean)annotation).value();
                    if(StringUtils.isNUll(id)) {
                        id=methods[i].getReturnType().getName();
                    }
                    annotationBean.setId(id);
                    annotationBean.setName(id);
                    annotationBean.setClassName(methods[i].getReturnType().getName());
                    Scope scope=methods[i].getDeclaredAnnotation(Scope.class);
                    if(scope!=null){
                        annotationBean.setScope(scope.value());
                    }
                    annotationBean.setInitMethod(((Bean)annotation).initMethod());
                    annotationBean.setAnnotations(annotations);
                    annotationBean.setParentName(parent.getName());
                    annotationBean.setIsMethodBean(true);
                    annotationBean.setMethodMetadata(MethodMetadata.createMethodMetadata(methods[i]));

                    if(methods[i].getReturnType().isInterface()){
                        this.getBeanFactory().putBeanNameOfType(methods[i].getReturnType().getName(),annotationBean);
                    }
                    this.getBeanFactory().putBeanDefinition(annotationBean);
                }
            }
        }
    }
    //注册Bean的接口
    public void registryBeanInterfaces(Class<?> cls, AnnotationBean annotationBean){
        Class<?>[] interfaces=cls.getInterfaces();
        if(interfaces==null){
            return;
        }
        for(Class interfaceCls:interfaces){
            this.getBeanFactory().putBeanNameOfType(interfaceCls.getName(),annotationBean);
        }
    }
    /**
     * 注册Bean
     * @param cls
     */
    public abstract void registry(Class<?> cls);
}
