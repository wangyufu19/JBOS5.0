package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.MethodAdvisor;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.transaction.DefaultTransactionDefinition;
import com.jbosframework.transaction.TransactionDefinition;
import com.jbosframework.transaction.TransactionManager;
import com.jbosframework.transaction.TransactionStatus;
import com.jbosframework.transaction.annotation.Transactional;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * TransactionMethodAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionMethodAdvice extends MethodAdvisor {
    private static final Log log= LogFactory.getLog(TransactionMethodAdvice.class);

    private BeanFactory beanFactory;
    private TransactionStatus transactionStatus;

    public TransactionMethodAdvice(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        //调用前
        this.before(object,method,args);
        Object result = doIntercept(object,method,args,methodProxy);
        //调用后
        this.after(object,method,args);
        return result;
    }
    private void before(Object target, Method method, Object[] args)  throws Exception{
        Transactional transactional=method.getDeclaredAnnotation(Transactional.class);
        if(transactional!=null){
            TransactionManager tx=beanFactory.getBean(DataSourceTransactionManager.class);
            TransactionDefinition transactionDefinition=new DefaultTransactionDefinition(transactional.propagation().getValue(),transactional.isolation().getValue());
            transactionStatus=tx.getTransaction(transactionDefinition);
        }
    }
    private void after(Object target, Method method, Object[] args) throws Exception{
        Transactional transactional=method.getDeclaredAnnotation(Transactional.class);
        if(transactional!=null){
            TransactionManager tx=beanFactory.getBean(DataSourceTransactionManager.class);
            tx.commit(transactionStatus);
        }
    }
}
