package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.ImportSelector;
import com.jbosframework.core.Ordered;
import com.jbosframework.jdbc.datasource.DataSourceTransactionManager;
import java.util.List;

public class TransactionManagerRegister implements ImportSelector, Ordered {
    private int order=Ordered.HIGHEST_PRECEDENCE+20;
    private ConfigurableApplicationContext applicationContext;

    public TransactionManagerRegister(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public List<String> processImports() {
        GenericBeanDefinition genericBeanDefinition=new GenericBeanDefinition(DataSourceTransactionManager.class);
        this.applicationContext.getBeanFactory().putBeanDefinition(genericBeanDefinition.getName(),genericBeanDefinition);
        TransactionBeanProcessor transactionBeanProcessor=new TransactionBeanProcessor();
        this.applicationContext.getBeanFactory().putBeanDefinition(
                transactionBeanProcessor.getClass().getName(),
                new GenericBeanDefinition(transactionBeanProcessor.getClass())
        );
        return null;
    }
    public int getOrder(){
        return this.order;
    }
}
