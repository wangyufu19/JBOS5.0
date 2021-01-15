package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.MethodAfterAdvice;
import com.jbosframework.aop.MethodBeforeAdvice;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.transaction.DefaultTransactionDefinition;
import com.jbosframework.transaction.TransactionDefinition;
import com.jbosframework.transaction.TransactionManager;
import com.jbosframework.transaction.TransactionStatus;
import com.jbosframework.transaction.annotation.Transactional;
import org.aopalliance.intercept.Joinpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * TransactionMethodAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionMethodAdvice{
    private static final Log log= LogFactory.getLog(TransactionMethodAdvice.class);
    private BeanFactory beanFactory;
    private TransactionStatus transactionStatus;

    public TransactionMethodAdvice(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }
    public class TransactionMethodBeforeAdvice implements MethodBeforeAdvice {
        private String adviceMethod;


        public void setAdviceMethod(String adviceMethod) {

        }

        public String getAdviceMethod() {
            return null;
        }

        public void before(Object target, Method method, Object[] args) throws Throwable {
            Transactional transactional=method.getDeclaredAnnotation(Transactional.class);
            if(transactional!=null){
                TransactionManager tx=beanFactory.getBean(DataSourceTransactionManager.class);
                TransactionDefinition transactionDefinition=new DefaultTransactionDefinition(transactional.propagation().getValue(),transactional.isolation().getValue());
                transactionStatus=tx.getTransaction(transactionDefinition);
            }
        }
    }
    public class TransactionMethodAfterAdvice implements MethodAfterAdvice{
        private String adviceMethod;

        private Joinpoint joinpoint;

        public void setAdviceMethod(String adviceMethod) {

        }

        public String getAdviceMethod() {
            return null;
        }

        public void after(Object target, Method method, Object[] args) throws Throwable {
            Transactional transactional=method.getDeclaredAnnotation(Transactional.class);
            if(transactional!=null){
                TransactionManager tx=beanFactory.getBean(DataSourceTransactionManager.class);
                tx.commit(transactionStatus);
            }
        }
    }

}
