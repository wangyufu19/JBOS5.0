//package com.jbosframework.boot.autoconfig.transaction;
//
//import com.jbosframework.aop.AdviceConfig;
//import com.jbosframework.aop.AopProxy;
//import com.jbosframework.aop.CglibProxy;
//import com.jbosframework.beans.config.BeanDefinition;
//import com.jbosframework.beans.config.BeanPostProcessor;
//import com.jbosframework.beans.factory.BeanFactory;
//import com.jbosframework.core.Ordered;
//import com.jbosframework.transaction.annotation.Transactional;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import java.lang.reflect.Method;
//
///**
// * TransactionBeanProcessor
// * @author youfu.wang
// * @version 5.0
// */
//public class TransactionBeanProcessor implements BeanPostProcessor {
//
//    private static final Log log= LogFactory.getLog(TransactionBeanProcessor.class);
//    private BeanFactory beanFactory;
//    private int order= Ordered.HIGHEST_PRECEDENCE;
//
//    public TransactionBeanProcessor(BeanFactory beanFactory){
//        this.beanFactory=beanFactory;
//    }
//
//    public void setOrder(int order){
//        this.order=order;
//    }
//    public int getOrder() {
//        return this.order;
//    }
//    public int compareTo(BeanPostProcessor beanPostProcessor) {
//        return this.order - beanPostProcessor.getOrder();
//    }
//    public Object process(Object obj, BeanDefinition beanDefinition){
//        boolean isTransactionalBean=false;
//        Object target = obj;
//        Class<?> cls=obj.getClass();
//        if(cls==null){
//            return target;
//        }
//        Transactional transactional = cls.getDeclaredAnnotation(Transactional.class);
//        if(transactional!=null){
//            isTransactionalBean=true;
//        }else{
//            Method[] methods=cls.getDeclaredMethods();
//            if(methods==null){
//                return target;
//            }
//            for(Method method:methods) {
//                transactional = method.getDeclaredAnnotation(Transactional.class);
//                if(transactional!=null){
//                    isTransactionalBean=true;
//                    break;
//                }
//            }
//        }
//        if(isTransactionalBean){
//            AdviceConfig adviceConfig = new AdviceConfig();
//            TransactionMethodAdvice transactionMethodAdvice=new TransactionMethodAdvice(this.beanFactory);
//            transactionMethodAdvice.setTarget(obj);
//            adviceConfig.setTarget(obj);
//            adviceConfig.setMethodAdvisor(transactionMethodAdvice);
//            AopProxy aopProxy = new CglibProxy(adviceConfig);
//            target=aopProxy.getProxy();
//        }
//        return target;
//    }
//}
