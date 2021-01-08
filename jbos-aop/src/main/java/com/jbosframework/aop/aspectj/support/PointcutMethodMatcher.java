package com.jbosframework.aop.aspectj.support;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.aspectj.AspectAdvice;
import com.jbosframework.aop.aspectj.AspectJMethodInvocation;
import com.jbosframework.aop.aspectj.AspectjMethodBeforeAdvice;
import com.jbosframework.aop.support.ProxyConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;

/**
 * PointcutMethodMatcher
 * @author youfu.wang
 * @version 5.0
 */
public class PointcutMethodMatcher {
    private static Log log= LogFactory.getLog(PointcutMethodMatcher.class);
    private AspectProxyBeanContext aspectProxyBeanContext;
    private AspectMetadata aspectMetadata;

    /**
     * 构造方法
     * @param aspectProxyBeanContext
     */
    public PointcutMethodMatcher(AspectProxyBeanContext aspectProxyBeanContext){
        this.aspectProxyBeanContext=aspectProxyBeanContext;
    }
    /**
     * 捕获Bean对象切入点方法
     * @return
     */
    public boolean match(Object obj){
        boolean bool=false;
        if(obj==null){
            return false;
        }
        Method[] methods=obj.getClass().getDeclaredMethods();
        if(methods==null){
            return false;
        }
        for(Method method:methods){
            String pointcut=obj.getClass().getName()+"."+method.getName();
            if(aspectProxyBeanContext.contains(pointcut)){
                aspectMetadata=aspectProxyBeanContext.getMetadata(pointcut);
                aspectMetadata.getAspectAdvice().setMethod(method.getName());
                bool=true;
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
    public Object getAspectAopProxy(Object obj){
        AspectAdvice aspectAdvice=aspectMetadata.getAspectAdvice();
        aspectAdvice.setTarget(obj);
        AopProxy aopProxy=new AspectCglibProxy(aspectAdvice);
        return aopProxy.getProxy();
    }
}
