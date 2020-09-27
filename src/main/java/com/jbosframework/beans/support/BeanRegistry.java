package com.jbosframework.beans.support;

import com.jbosframework.beans.annotation.Scope;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.common.utils.StringUtils;
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
    public void setBeanScope(Class<?> annClass,AnnotationBean annotationBean){
        Scope scope=annClass.getAnnotation(Scope.class);
        if(scope!=null){
            annotationBean.setScope(scope.value());
        }
    }
    /**
     * 注册Bean
     * @param annotationBean
     */
    public void registryBean(AnnotationBean annotationBean){
        if(!"".equals(StringUtils.replaceNull(annotationBean.getId()))||!"".equals(StringUtils.replaceNull(annotationBean.getName()))){
            this.ctx.putBeanDefinition(annotationBean);
        }
    }
}
