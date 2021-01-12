package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import com.jbosframework.transaction.TransactionDefinition;
import com.jbosframework.transaction.TransactionManager;
import com.jbosframework.utils.JBOSClassCaller;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * DataSourceTransactionAdvice
 * @author youfu.wang
 * @version 5.0
 */
public class DataSourceTransactionAdvice implements MethodInvocation {
    private BeanFactory beanFactory;
    private TransactionManager tx;
    private String method;
    private Object[] args;


    public DataSourceTransactionAdvice(BeanFactory beanFactory,String method,Object[] args){
        this.beanFactory=beanFactory;
        this.method=method;
        this.args=args;
    }

    public Method getMethod() {
        return null;
    }

    public Object[] getArguments() {
        return this.args;
    }

    public Object proceed() throws Throwable {
        tx=this.beanFactory.getBean(DataSourceTransactionManager.class);
        return JBOSClassCaller.call(tx,this.method,this.args);
    }

    public Object getThis() {
        return this;
    }

    public AccessibleObject getStaticPart() {
        return null;
    }
}
