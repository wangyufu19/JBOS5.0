package com.jbosframework.aspectj.support;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.context.support.BeanFactoryContext;
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
    private PathMatcher pathMatcher=new AntPathMatcher();
    private AspectMetadata aspectMetadata;


    /**
     * 捕获Bean对象切入点方法
     * @return
     */
    public boolean match(BeanFactoryContext context, Object obj){
        boolean bool=false;
        Method[] methods=obj.getClass().getMethods();
        if(methods!=null){
            for(Method method:methods){
                String pointcut=obj.getClass().getName()+"."+method.getName();
                if(context.getAspectProxyBeanContext().contains(pointcut)){
                    aspectMetadata=context.getAspectProxyBeanContext().getMetadata(pointcut);
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
        return aopProxy.createProxy();
    }

}
