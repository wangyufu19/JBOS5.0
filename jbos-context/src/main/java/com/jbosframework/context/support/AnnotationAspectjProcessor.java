package com.jbosframework.context.support;

import com.jbosframework.aop.aspectj.support.PointcutMethodMatcher;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.core.Order;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AnnotationAspectjProcessor
 * @author youfu.wang
 * @version 5.0
 */
public class AnnotationAspectjProcessor implements BeanPostProcessor {
    private static final Log log= LogFactory.getLog(AnnotationAspectjProcessor.class);
    private ApplicationContext applicationContext;
    private int order= Order.MIN;

    public AnnotationAspectjProcessor(ApplicationContext ApplicationContext){
        this.applicationContext=ApplicationContext;
    }

    public void setOrder(int order){
        this.order=order;
    }
    public int getOrder() {
        return this.order;
    }
    public int compareTo(BeanPostProcessor beanPostProcessor) {
        return this.order - beanPostProcessor.getOrder();
    }
    public Object process(Object obj){
        Object target=obj;
        PointcutMethodMatcher pointcutMethodMatcher=new PointcutMethodMatcher(this.applicationContext.getAspectProxyBeanContext());
        if(pointcutMethodMatcher.match(target)){
            //判断是否切面AOP代理Bean
            target=pointcutMethodMatcher.getAspectAopProxy(target);
        }
        return target;
    }
}
