package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.aspectj.AspectjMethodAfterAdvice;
import com.jbosframework.aop.aspectj.AspectjMethodBeforeAdvice;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.aop.aspectj.support.AspectProxyBeanContext;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.transaction.DefaultTransactionDefinition;
import com.jbosframework.transaction.TransactionDefinition;
import com.jbosframework.transaction.TransactionManager;
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
                TransactionManager tx=this.getBeanFactory().getBean(DataSourceTransactionManager.class);
                AspectMetadata aspectMetadata=new AspectMetadata();
                aspectMetadata.setAspectClass(cls);
                String pointcut=cls+"."+method.getName();
                aspectMetadata.setPointcut(pointcut);

                AspectjMethodBeforeAdvice methodBeforeAdvice=new AspectjMethodBeforeAdvice();
                methodBeforeAdvice.setTarget(tx);
                methodBeforeAdvice.setMethod("getTransaction");
                methodBeforeAdvice.setArgs(new Object[]{transactionDefinition});
                aspectMetadata.setMethodBeforeAdvice(methodBeforeAdvice);

                AspectjMethodAfterAdvice methodAfterAdvice=new AspectjMethodAfterAdvice();
                methodBeforeAdvice.setTarget(tx);
                methodAfterAdvice.setMethod("commit");
                methodBeforeAdvice.setArgs(new Object[]{transactionDefinition});
                aspectMetadata.setMethodAfterAdvice(methodAfterAdvice);

                this.aspectProxyBeanContext.putMetadata(aspectMetadata);
            }
        }
    }
}
