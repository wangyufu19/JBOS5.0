package com.jbosframework.schedule;

import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.CglibProxy;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.core.Order;
import com.jbosframework.schedule.annotation.Async;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;


/**
 * AsyncBeanProcessor
 * @author youfu.wang
 * @version 5.0
 */
public class AsyncBeanProcessor implements BeanPostProcessor {
    private static final Log log= LogFactory.getLog(AsyncBeanProcessor.class);
    private BeanFactory beanFactory;
    private int order= Order.MIN;

    public AsyncBeanProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
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
    public Object process(Object obj) {
        boolean isAsyncBean=false;
        Object target = obj;
        Class<?> cls=obj.getClass();
        if(cls==null){
            return target;
        }
        Method[] methods=cls.getDeclaredMethods();
        if(methods==null){
            return target;
        }
        for(Method method:methods) {
            Async async = method.getDeclaredAnnotation(Async.class);
            if(async!=null){
                isAsyncBean=true;
                break;
            }
        }
        if(isAsyncBean){
            AdviceConfig adviceConfig = new AdviceConfig();
            AsyncMethodAdvice asyncMethodAdvice=new AsyncMethodAdvice(this.beanFactory);
            adviceConfig.setMethodCaller(asyncMethodAdvice);
            adviceConfig.setTarget(obj);
            AopProxy aopProxy = new CglibProxy(adviceConfig);
            target=aopProxy.getProxy();
        }
        return target;
    }
}
