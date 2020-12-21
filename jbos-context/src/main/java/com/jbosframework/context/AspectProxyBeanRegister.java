package com.jbosframework.context;
import com.jbosframework.aop.aspectj.Aspect;
import com.jbosframework.aop.aspectj.Pointcut;
import com.jbosframework.aop.aspectj.Before;
import com.jbosframework.aop.aspectj.After;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.aop.aspectj.support.AspectProxyBeanContext;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.utils.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * AopProxyBeanRegister
 * @author youfu.wang
 * @version 1.0
 */
public class AspectProxyBeanRegister extends BeanRegistry {
    private static Log log=LogFactory.getLog(AspectProxyBeanRegister.class);
    private AspectProxyBeanContext aspectProxyBeanContext;
    /**
     * 构造方法
     */
    public AspectProxyBeanRegister(AspectProxyBeanContext aspectProxyBeanContext){
        this.aspectProxyBeanContext=aspectProxyBeanContext;
    }
    /**
     * 注册Bean
     * @param cls
     */
    public void registerBean(Class<?> cls){
        Aspect aspect=cls.getAnnotation(Aspect.class);
        if(aspect==null){
            return;
        }
        AspectMetadata metadata=new AspectMetadata();
        metadata.setAspectClass(cls);
        this.registerMethodMetadata(cls,metadata);
        if(!"".equals(metadata.getPointcut())){
            log.debug("******注入切面类["+cls.getName()+"]");
            this.aspectProxyBeanContext.putMetadata(metadata);
        }
    }
    private void registerMethodMetadata(Class<?> cls, AspectMetadata metadata){
        if(cls==null){
            return;
        }
        Method[] methods=cls.getMethods();
        if(methods==null) {
            return;
        }
        for(int i=0;i<methods.length;i++) {
            Annotation[] annotations = methods[i].getAnnotations();
            if (annotations == null) {
                continue;
            }
            for(int j=0;j<annotations.length;j++) {
                if (annotations[j] instanceof Pointcut) {
                    Pointcut pointcut=(Pointcut)annotations[j];
                    metadata.setPointcut(StringUtils.replaceNull(pointcut.value()));
                }else if(annotations[j] instanceof Before){
                    Before before=(Before)annotations[j];
                    metadata.setBeforeMethod(methods[i].getName());
                }else if(annotations[j] instanceof After){
                    After after=(After)annotations[j];
                    metadata.setAfterMethod(methods[i].getName());
                }
            }
        }
    }
}
