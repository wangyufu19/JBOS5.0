package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.aspectj.AspectJMethodInvocation;
import com.jbosframework.aop.aspectj.AspectjMethodAfterAdvice;
import com.jbosframework.aop.aspectj.AspectjMethodBeforeAdvice;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.aop.aspectj.support.AspectProxyBeanContext;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.transaction.DefaultTransactionDefinition;
import com.jbosframework.transaction.TransactionDefinition;
import com.jbosframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;

/**
 * TransactionAdviceRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionAdviceRegistry extends BeanRegistry {

    private static final Log log= LogFactory.getLog(TransactionAdviceRegistry.class);
    private AspectProxyBeanContext aspectProxyBeanContext;
    /**
     * 构造方法
     */
    public TransactionAdviceRegistry(AspectProxyBeanContext aspectProxyBeanContext){
        this.aspectProxyBeanContext=aspectProxyBeanContext;
    }

    public void registerBean(Class<?> cls){
        if(cls==null){
            return;
        }
        Method[] methods=cls.getMethods();
        if(methods==null) {
            return;
        }
        Transactional transactional = null;
        for(Method method:methods){
            transactional = method.getDeclaredAnnotation(Transactional.class);
            if (transactional != null) {
                TransactionDefinition transactionDefinition=new DefaultTransactionDefinition(transactional.propagation().getValue(),transactional.isolation().getValue());
                AspectMetadata aspectMetadata=new AspectMetadata();
                String pointcut=cls+"."+method.getName();
                aspectMetadata.setPointcut(pointcut);
                AdviceConfig aspectAdvice=new AdviceConfig();
                AspectJMethodInvocation aspectJBeforePointcut=new AspectJMethodInvocation(DataSourceTransactionManager.class,method,new Object[]{transactionDefinition});
                AspectjMethodBeforeAdvice aspectjMethodBeforeAdvice=new AspectjMethodBeforeAdvice(aspectJBeforePointcut);
                aspectAdvice.setMethodBeforeAdvice(aspectjMethodBeforeAdvice);

                AspectJMethodInvocation aspectJAfterPointcut=new AspectJMethodInvocation(cls,method,new Object[]{transactionDefinition});
                AspectjMethodAfterAdvice aspectjMethodAfterAdvice=new AspectjMethodAfterAdvice(aspectJAfterPointcut);
                aspectAdvice.setMethodAfterAdvice(aspectjMethodAfterAdvice);

                this.aspectProxyBeanContext.putMetadata(aspectMetadata);
            }
        }
    }
}
