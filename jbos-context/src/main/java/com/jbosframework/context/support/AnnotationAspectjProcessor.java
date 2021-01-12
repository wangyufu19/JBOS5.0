package com.jbosframework.context.support;

import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.aspectj.AspectAdvice;
import com.jbosframework.aop.aspectj.support.AspectCglibProxy;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.core.Order;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

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
        PointcutMethodMatcher pointcutMethodMatcher=new PointcutMethodMatcher(this.applicationContext);
        if(pointcutMethodMatcher.match(target)){
            //判断是否切面AOP代理Bean
            target=pointcutMethodMatcher.getAspectAopProxy(target);
        }
        return target;
    }
    /**
     * PointcutMethodMatcher
     * @author youfu.wang
     * @version 5.0
     */
    public class PointcutMethodMatcher {
        private ApplicationContext applicationContext;
        private AspectMetadata aspectMetadata;

        /**
         * 构造方法
         *
         * @param applicationContext
         */
        public PointcutMethodMatcher(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        /**
         * 捕获Bean对象切入点方法
         *
         * @return
         */
        public boolean match(Object obj) {
            boolean bool = false;
            if (obj == null) {
                return false;
            }
            Method[] methods = obj.getClass().getDeclaredMethods();
            if (methods == null) {
                return false;
            }
            for (Method method : methods) {
                String pointcut = obj.getClass().getName() + "." + method.getName();
                if (this.applicationContext.getAspectProxyBeanContext().contains(pointcut)) {
                    aspectMetadata = this.applicationContext.getAspectProxyBeanContext().getMetadata(pointcut);
                    aspectMetadata.getAspectAdvice().setMethod(method.getName());
                    bool = true;
                    break;
                }
            }
            return bool;
        }

        /**
         * 得到AOP代理对象
         * @param obj
         * @return
         */
        public Object getAspectAopProxy(Object obj) {
            AspectAdvice aspectAdvice = aspectMetadata.getAspectAdvice();
            aspectAdvice.setTarget(obj);
            AopProxy aopProxy = new AspectCglibProxy(aspectAdvice);
            return aopProxy.getProxy();
        }
    }
}
