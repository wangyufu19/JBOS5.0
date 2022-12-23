package com.jbosframework.boot.autoconfig.transaction;

import com.jbosframework.aop.AdviceConfig;
import com.jbosframework.aop.AopProxy;
import com.jbosframework.aop.CglibProxy;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextAware;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
import com.jbosframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * TransactionBeanProcessor
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionBeanProcessor implements ApplicationContextAware, BeanPostProcessor, Ordered {
    private static final Log logger= LogFactory.getLog(TransactionBeanProcessor.class);
    private int order=Ordered.HIGHEST_PRECEDENCE+60;
    private ConfigurableApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=(ConfigurableApplicationContext)applicationContext;
    }
    public int getOrder() {
        return this.order;
    }
    public Object postProcessBeforeInitialization(Object bean,BeanDefinition beanDefinition){
        boolean isTransactionalBean=false;
        Class<?> cls=bean.getClass();
        if(cls==null){
            return bean;
        }
        Transactional transactional = cls.getDeclaredAnnotation(Transactional.class);
        if(transactional!=null){
            isTransactionalBean=true;
        }else{
            Method[] methods=cls.getDeclaredMethods();
            if(methods==null){
                return bean;
            }
            for(Method method:methods) {
                transactional = method.getDeclaredAnnotation(Transactional.class);
                if(transactional!=null){
                    isTransactionalBean=true;
                    break;
                }
            }
        }
        if(isTransactionalBean){
            AdviceConfig adviceConfig = new AdviceConfig();
            TransactionMethodAdvice transactionMethodAdvice=new TransactionMethodAdvice(this.applicationContext);
            transactionMethodAdvice.setTarget(bean);
            adviceConfig.setTarget(bean);
            adviceConfig.setMethodAdvisor(transactionMethodAdvice);
            AopProxy aopProxy = new CglibProxy(adviceConfig);
            bean=aopProxy.getProxy();
        }
        return bean;
    }
}
