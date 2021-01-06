package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.aspectj.AspectjMethodAfterAdvice;
import com.jbosframework.aop.aspectj.AspectjMethodBeforeAdvice;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.transaction.TransactionManager;
import com.jbosframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;

/**
 * TransactionAdviceProcessor
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionAdviceProcessor implements BeanPostProcessor {

    private static final Log log= LogFactory.getLog(TransactionAdviceProcessor.class);
    private ApplicationContext applicationContext;

    public TransactionAdviceProcessor(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    public void process(Object obj){
        if(obj==null){
            return;
        }
        Method[] methods=obj.getClass().getMethods();
        if(methods==null) {
            return;
        }
        Transactional transactional = null;
        for(Method method:methods){
            transactional = method.getDeclaredAnnotation(Transactional.class);
            if (transactional != null) {
                TransactionManager tx=this.applicationContext.getBean(DataSourceTransactionManager.class);
                AspectMetadata aspectMetadata=new AspectMetadata();
                aspectMetadata.setAspectClass(obj.getClass());
                aspectMetadata.setPointcut(obj.getClass().getName()+"."+method.getName());
                AspectjMethodBeforeAdvice methodBeforeAdvice=new AspectjMethodBeforeAdvice();
                methodBeforeAdvice.setMethod(method);
                aspectMetadata.setMethodBeforeAdvice(methodBeforeAdvice);
                AspectjMethodAfterAdvice methodAfterAdvice=new AspectjMethodAfterAdvice();
                methodAfterAdvice.setMethod(method);
                aspectMetadata.setMethodAfterAdvice(methodAfterAdvice);
                applicationContext.getAspectProxyBeanContext().putMetadata(aspectMetadata);
            }
        }
    }
}
