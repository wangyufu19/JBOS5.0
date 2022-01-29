//package com.jbosframework.schedule.annotation;
//import com.jbosframework.context.annotation.Configuration;
//import com.jbosframework.context.support.BeanProcessorRegistry;
//import com.jbosframework.schedule.AsyncBeanProcessor;
//
///**
// * AsyncConfiguration
// * @author youfu.wang
// * @version 5.0
// */
//@Configuration
//public class AsyncConfiguration extends BeanProcessorRegistry {
//
//    public void registryBeanProcessor(){
//        AsyncBeanProcessor asyncBeanProcessor=new AsyncBeanProcessor(this.getBeanFactory());
//        asyncBeanProcessor.setOrder(25);
//        this.getBeanFactory().addBeanPostProcessor(asyncBeanProcessor);
//    }
//}
