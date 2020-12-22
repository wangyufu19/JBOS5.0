package com.jbosframework.aop.aspectj.support;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.utils.AntPathMatcher;
import com.jbosframework.utils.PathMatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;

/**
 * PointcutMethodMatcher
 * @author youfu.wang
 * @version 1.0
 */
public class PointcutMethodMatcher {
    private static Log log= LogFactory.getLog(PointcutMethodMatcher.class);
    private AspectProxyBeanContext aspectProxyBeanContext;
    private PathMatcher pathMatcher=new AntPathMatcher();
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
            return bool;
        }
        Method[] methods=obj.getClass().getMethods();
        if(methods!=null){
            for(Method method:methods){
                String pointcut=obj.getClass().getName()+"."+method.getName();
                if(aspectProxyBeanContext.contains(pointcut)){
                    aspectMetadata=aspectProxyBeanContext.getMetadata(pointcut);
                    bool=true;
                    break;
                }
            }
        }
        return bool;
    }

    /**
     * 得到AOP代理实例
     * @param obj
     * @return
     */
    public Object getAspectAopProxy(Object obj){
        AopProxy aopProxy=new AspectCglibProxy(aspectMetadata,obj.getClass());
        return aopProxy.getProxy();
    }

}
