package com.jbosframework.context.annotation.aspectj;

import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.CglibProxy;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.core.Ordered;
import com.jbosframework.schedule.annotation.async.AsyncBeanPostProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;

public class AspectjBeanPostProcessor implements BeanPostProcessor,Ordered {
    private static final Log logger= LogFactory.getLog(AsyncBeanPostProcessor.class);
    private AspectjApplicationContext aspectjApplicationContext;
    private int order=Ordered.HIGHEST_PRECEDENCE+50;

    public AspectjBeanPostProcessor(AspectjApplicationContext aspectjApplicationContext){
        this.aspectjApplicationContext=aspectjApplicationContext;
    }
    public int getOrder() {
        return this.order;
    }

    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition){
        Object target=bean;
        PointcutMethodMatcher pointcutMethodMatcher=new PointcutMethodMatcher(this.aspectjApplicationContext);
        if(pointcutMethodMatcher.match(target)){
            target=pointcutMethodMatcher.getAspectAopProxy(target);
        }
        return target;
    }
    public class PointcutMethodMatcher {
    private AspectjApplicationContext aspectjApplicationContext;
    private AspectMetadata aspectMetadata;

    public PointcutMethodMatcher(AspectjApplicationContext aspectjApplicationContext) {
        this.aspectjApplicationContext = aspectjApplicationContext;
    }
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
            if (this.aspectjApplicationContext.contains(pointcut)) {
                aspectMetadata = this.aspectjApplicationContext.getMetadata(pointcut);
                aspectMetadata.getAdviceConfig().getMethodAdvisor().setTarget(obj);
                aspectMetadata.getAdviceConfig().getMethodAdvisor().setAdviceMethod(method.getName());
                bool = true;
                break;
            }
        }
        return bool;
    }

    public Object getAspectAopProxy(Object obj) {
        AdviceConfig adviceConfig = aspectMetadata.getAdviceConfig();
        adviceConfig.setTarget(obj);
        AopProxy aopProxy = new CglibProxy(adviceConfig);
        return aopProxy.getProxy();
    }
}
}
