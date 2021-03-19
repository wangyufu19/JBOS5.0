package com.jbosframework.context.support;
import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.aspectj.*;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.utils.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * AspectProxyRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class AspectProxyRegistry extends BeanRegistry {
    private static Log log=LogFactory.getLog(AspectProxyRegistry.class);
    private AspectProxyContext aspectProxyBeanContext;
    /**
     * 构造方法
     */
    public AspectProxyRegistry(AspectProxyContext aspectProxyBeanContext){
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
        Method[] methods=cls.getDeclaredMethods();
        if(methods==null) {
            return;
        }
        AspectMetadata metadata=new AspectMetadata();
        AdviceConfig adviceConfig=new AdviceConfig();
        AspectjMethodAdvice aspectjMethodAdvice=new AspectjMethodAdvice();
        for(int i=0;i<methods.length;i++) {
            Annotation[] annotations = methods[i].getAnnotations();
            if (annotations == null) {
                continue;
            }
            for(int j=0;j<annotations.length;j++) {
                if (annotations[j] instanceof Pointcut) {
                    Pointcut pointcut=(Pointcut)annotations[j];
                    metadata.setPointcut(StringUtils.replaceNull(pointcut.value()));
                }
                if(annotations[j] instanceof Before){
                    Before pointcut=(Before)annotations[j];
                    if(StringUtils.isNotNUll(pointcut.value())){
                        metadata.setPointcut(StringUtils.replaceNull(pointcut.value()));
                    }
                    AspectJMethodInvocation aspectJPointcut=new AspectJMethodInvocation(cls,methods[i],null);
                    AspectjMethodBeforeAdvice aspectjMethodBeforeAdvice=new AspectjMethodBeforeAdvice(aspectJPointcut);
                    aspectjMethodAdvice.setAspectjMethodBeforeAdvice(aspectjMethodBeforeAdvice);
                }else if(annotations[j] instanceof After){
                    After pointcut=(After)annotations[j];
                    if(StringUtils.isNotNUll(pointcut.value())){
                        metadata.setPointcut(StringUtils.replaceNull(pointcut.value()));
                    }
                    AspectJMethodInvocation aspectJPointcut=new AspectJMethodInvocation(cls,methods[i],null);
                    AspectjMethodAfterAdvice aspectjMethodAfterAdvice=new AspectjMethodAfterAdvice(aspectJPointcut);
                    aspectjMethodAdvice.setAspectjMethodAfterAdvice(aspectjMethodAfterAdvice);
                }
            }
        }
        adviceConfig.setMethodAdvisor(aspectjMethodAdvice);
        metadata.setAdviceConfig(adviceConfig);
        if(!"".equals(metadata.getPointcut())){
            log.debug("******注入切面类["+cls.getName()+"]");
            this.aspectProxyBeanContext.putMetadata(metadata);
        }
    }
}
