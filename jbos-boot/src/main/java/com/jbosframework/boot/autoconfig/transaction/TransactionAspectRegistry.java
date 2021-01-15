package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.aop.aspectj.support.AspectProxyBeanContext;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * TransactionAspectRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionAspectRegistry extends BeanRegistry {

    private static Log log= LogFactory.getLog(TransactionAspectRegistry.class);

    private ApplicationContext applicationContext;
    /**
     * 构造方法
     */
    public TransactionAspectRegistry(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public void registerBean(Class<?> cls) {
        if(cls==null){
            return;
        }
        Method[] methods=cls.getDeclaredMethods();
        if(methods==null){
            return;
        }
        for(Method method:methods){
            Transactional transactional=method.getDeclaredAnnotation(Transactional.class);
            if(transactional!=null){
                AspectMetadata metadata=new AspectMetadata();
                AdviceConfig adviceConfig=new AdviceConfig();
                metadata.setPointcut(cls.getName()+"."+method.getName());
                TransactionMethodAdvice transactionMethodAdvice=new TransactionMethodAdvice(this.applicationContext);
                adviceConfig.setMethodBeforeAdvice(transactionMethodAdvice.new TransactionMethodBeforeAdvice());
                adviceConfig.setMethodAfterAdvice(transactionMethodAdvice.new TransactionMethodAfterAdvice());
                metadata.setAdviceConfig(adviceConfig);
                if(!"".equals(metadata.getPointcut())){
                    log.debug("******注入切面类["+cls.getName()+"]");
                    this.applicationContext.getAspectProxyBeanContext().putMetadata(metadata);
                }
            }
        }
    }
}
