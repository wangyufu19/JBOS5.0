package com.jbosframework.beans.support;

import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.utils.StringUtils;
/**
 * BeanRegistry
 * @author youfu.wang
 * @version 1.0
 */
public class BeanRegistry {
    private ApplicationContext ctx;

    /**
     * 构造方法
     * @param ctx
     */
    public BeanRegistry(ApplicationContext ctx){
        this.ctx=ctx;
    }
    /**
     * 设置Scope注解(singleton,prototype)
     * @param annClass
     * @param annotationBean
     */
    private void setBeanScope(Class<?> annClass,AnnotationBean annotationBean){
        Scope scope=annClass.getAnnotation(Scope.class);
        if(scope!=null){
            annotationBean.setScope(scope.value());
        }
    }
    /**
     * 注册Bean
     * @param annotationBean
     */
    public void registryBean(Class<?> annClass,AnnotationBean annotationBean){
        if(!"".equals(StringUtils.replaceNull(annotationBean.getId()))||!"".equals(StringUtils.replaceNull(annotationBean.getName()))){
            this.setBeanScope(annClass,annotationBean);
            this.ctx.putBeanDefinition(annotationBean);
        }
    }
    //注册Bean的接口
    public void registryBeanInterfaces(Class<?> cls,AnnotationBean annotationBean){
        Class<?>[] interfaces=cls.getInterfaces();
        if(interfaces==null){
            return;
        }
        for(Class interfaceCls:interfaces){
            this.ctx.putBeanNameOfType(interfaceCls.getName(),annotationBean);
        }
    }
    //注册Bean的接口
    public void registryBeanInterfaces(String interfaceName,AnnotationBean annotationBean){
        this.ctx.putBeanNameOfType(interfaceName,annotationBean);
    }
}
