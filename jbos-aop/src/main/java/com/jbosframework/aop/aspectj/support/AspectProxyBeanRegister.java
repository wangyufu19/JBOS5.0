package com.jbosframework.aop.aspectj.support;
import com.jbosframework.aop.aspectj.*;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.utils.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * AopProxyBeanRegister
 * @author youfu.wang
 * @version 5.0
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
        this.doRegisterBean(cls);
    }
    private void doRegisterBean(Class<?> cls){
        if(cls==null){
            return;
        }
        Method[] methods=cls.getMethods();
        if(methods==null) {
            return;
        }
        AspectMetadata metadata=new AspectMetadata();
        metadata.setAspectClass(cls);
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
                    AspectjMethodBeforeAdvice methodBeforeAdvice=new AspectjMethodBeforeAdvice();
                    methodBeforeAdvice.setTargetClass(cls);
                    methodBeforeAdvice.setMethod(methods[i]);
                    metadata.setMethodBeforeAdvice(methodBeforeAdvice);
                }else if(annotations[j] instanceof After){
                    AspectjMethodAfterAdvice methodAfterAdvice=new AspectjMethodAfterAdvice();
                    methodAfterAdvice.setTargetClass(cls);
                    methodAfterAdvice.setMethod(methods[i]);
                    metadata.setMethodAfterAdvice(methodAfterAdvice);
                }
            }
        }
        if(!"".equals(metadata.getPointcut())){
            log.debug("******注入切面类["+cls.getName()+"]");
            this.aspectProxyBeanContext.putMetadata(metadata);
        }
    }
}
