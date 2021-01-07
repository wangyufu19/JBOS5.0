package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.aspectj.AspectjMethodAfterAdvice;
import com.jbosframework.aop.aspectj.AspectjMethodBeforeAdvice;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.core.Order;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.transaction.DefaultTransactionDefinition;
import com.jbosframework.transaction.TransactionDefinition;
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
    private int order= Order.MIN;

    public TransactionAdviceProcessor(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public void setOrder(int order){
        this.order=order;
    }
    public int getOrder() {
        return this.order;
    }
    public int compareTo(BeanPostProcessor beanPostProcessor) {
        return this.order - beanPostProcessor.getOrder();
    }
    public Object process(Object obj){
        Object target=obj;
        if(obj==null){
            return null;
        }
        Method[] methods=obj.getClass().getMethods();
        if(methods==null) {
            return target;
        }
        Transactional transactional = null;
        for(Method method:methods){
            transactional = method.getDeclaredAnnotation(Transactional.class);
            if (transactional != null) {
                TransactionDefinition transactionDefinition=new DefaultTransactionDefinition(transactional.propagation().getValue(),transactional.isolation().getValue());
                TransactionManager tx=this.applicationContext.getBean(DataSourceTransactionManager.class);
                AspectMetadata aspectMetadata=new AspectMetadata();
                aspectMetadata.setAspectClass(obj.getClass());
                aspectMetadata.setPointcut(obj.getClass().getName()+"."+method.getName());

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

                applicationContext.getAspectProxyBeanContext().putMetadata(aspectMetadata);
            }
        }
        return target;
    }
}
