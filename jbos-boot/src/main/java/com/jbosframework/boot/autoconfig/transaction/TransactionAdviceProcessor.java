package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanAutowiredProcessor;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.core.Order;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TransactionAdviceProcessor
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionAdviceProcessor implements BeanPostProcessor {
    private static final Log log= LogFactory.getLog(BeanAutowiredProcessor.class);
    private BeanFactory beanFactory;
    private int order= Order.MIN;

    public TransactionAdviceProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
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
    public Object process(Object obj) {
        Object target = obj;
        if (obj == null) {
            return null;
        }
        Class<?> cls = target.getClass();

        return target;
    }
}
