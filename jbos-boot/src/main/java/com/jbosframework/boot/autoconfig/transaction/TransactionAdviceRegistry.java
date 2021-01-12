package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.aspectj.AspectjMethodAfterAdvice;
import com.jbosframework.aop.aspectj.AspectjMethodBeforeAdvice;
import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.transaction.DefaultTransactionDefinition;
import com.jbosframework.transaction.TransactionDefinition;
import com.jbosframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;
import com.jbosframework.boot.autoconfig.transaction.DataSourceTransactionAdvice;
/**
 * TransactionAdviceRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionAdviceRegistry extends BeanRegistry {

    private static final Log log= LogFactory.getLog(TransactionAdviceRegistry.class);
    private ApplicationContext applicationContext;
    /**
     * 构造方法
     */
    public TransactionAdviceRegistry(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
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
                AspectjMethodBeforeAdvice aspectjMethodBeforeAdvice=new AspectjMethodBeforeAdvice(new DataSourceTransactionAdvice(this.applicationContext,"getTransaction",new Object[]{transactionDefinition}));
                aspectAdvice.setMethodBeforeAdvice(aspectjMethodBeforeAdvice);

                AspectjMethodAfterAdvice aspectjMethodAfterAdvice=new AspectjMethodAfterAdvice(new DataSourceTransactionAdvice(this.applicationContext,"commit",new Object[]{transactionDefinition}));
                aspectAdvice.setMethodAfterAdvice(aspectjMethodAfterAdvice);

                this.applicationContext.getAspectProxyBeanContext().putMetadata(aspectMetadata);
            }
        }
    }
}
