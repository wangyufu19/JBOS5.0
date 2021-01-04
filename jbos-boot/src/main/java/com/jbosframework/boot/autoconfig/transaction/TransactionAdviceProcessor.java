package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.CglibProxy;
import com.jbosframework.aop.support.ProxyConfig;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanFactory;
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
    private BeanFactory beanFactory;

    public TransactionAdviceProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
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
                break;
            }
        }

        if(transactional!=null){
            log.info("********obj: "+obj);
            ProxyConfig proxyConfig=new ProxyConfig();
            proxyConfig.setTarget(obj);
            TransactionAdviceProxy transactionAdviceProxy=new TransactionAdviceProcessor.TransactionAdviceProxy(this.beanFactory.getBean(DataSourceTransactionManager.class));
            obj=transactionAdviceProxy.delegate(proxyConfig);
        }
    }
    public class TransactionAdviceProxy {
        private TransactionManager transactionManager;

        public TransactionAdviceProxy(TransactionManager transactionManager){
            this.transactionManager=transactionManager;
        }
        public  <T> T delegate(ProxyConfig proxyConfig){
            AopProxy aopProxy=new CglibProxy(proxyConfig);
            return aopProxy.getProxy();
        }
    }
}
