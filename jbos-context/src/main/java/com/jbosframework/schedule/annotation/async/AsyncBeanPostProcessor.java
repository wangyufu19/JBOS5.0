package com.jbosframework.schedule.annotation.async;

import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.CglibProxy;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.core.Ordered;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;


/**
 * AsyncBeanProcessor
 * @author youfu.wang
 * @version 5.0
 */
public class AsyncBeanPostProcessor implements BeanPostProcessor, Ordered {
    private static final Log logger= LogFactory.getLog(AsyncBeanPostProcessor.class);
    private BeanFactory beanFactory;
    private int order=Ordered.HIGHEST_PRECEDENCE+30;

    public AsyncBeanPostProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public int getOrder() {
        return this.order;
    }
    public Object postProcessBeforeInitialization(Object bean,BeanDefinition beanDefinition){
        boolean isAsyncBean=false;
        Object target = bean;
        Class<?> cls=target.getClass();
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
            asyncMethodAdvice.setTarget(target);
            adviceConfig.setTarget(target);
            adviceConfig.setMethodAdvisor(asyncMethodAdvice);
            AopProxy aopProxy = new CglibProxy(adviceConfig);
            target=aopProxy.getProxy();
        }
        return target;
    }
}
