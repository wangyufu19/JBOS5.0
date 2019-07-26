package com.jbosframework.aspectj.support;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aspectj.metadata.Metadata;
import java.lang.reflect.Method;

/**
 * AspectProxySupport
 * @author youfu.wang
 * @version 1.0
 */
public class AspectProxySupport {
    /**
     * 判断是否切面切入点
     * @param obj
     * @return
     */
    public static boolean isAspectPointcut(AspectProxyBeanContext context,Object obj){
        Metadata metadata=null;
        metadata=getAspectMetadata(context,obj);
        if(metadata!=null){
            return true;
        }
        return false;
    }
    /**
     * 得到AOP代理实例
     * @param obj
     * @return
     */
    public static Object getAspectAopProxy(AspectProxyBeanContext context,Object obj){
        if(obj==null){
            return null;
        }
        Metadata metadata=null;
        metadata=getAspectMetadata(context,obj);
        AopProxy aopProxy=new AspectCglibProxy(metadata,obj.getClass());
        return aopProxy.createProxy();
    }
    private static Metadata getAspectMetadata(AspectProxyBeanContext context,Object obj){
        Metadata metadata=null;
        Method[] methods=null;
        if(obj!=null){
            methods=obj.getClass().getMethods();
            if(methods==null){
                return null;
            }
        }
        if(methods==null){
            return null;
        }
        for(Method method:methods){
            String pointcut=obj.getClass().getName()+"."+method.getName();
            if(context.contains(pointcut)){
                metadata=context.getMetadata(pointcut);
                break;
            }
        }
        return metadata;
    }
}
