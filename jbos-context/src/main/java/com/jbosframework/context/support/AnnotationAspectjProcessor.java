package com.jbosframework.context.support;

import com.jbosframework.aop.aspectj.support.PointcutMethodMatcher;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.ApplicationContext;

/**
 * AnnotationAspectjProcessor
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationAspectjProcessor implements BeanPostProcessor {
    private ApplicationContext applicationContext;

    public AnnotationAspectjProcessor(ApplicationContext ApplicationContext){
        this.applicationContext=ApplicationContext;
    }
    public void process(Object obj){
        PointcutMethodMatcher pointcutMethodMatcher=new PointcutMethodMatcher(this.applicationContext.getAspectProxyBeanContext());
        if(pointcutMethodMatcher.match(obj)){
            //判断是否切面AOP代理Bean
            obj=pointcutMethodMatcher.getAspectAopProxy(obj);
        }
    }
}
