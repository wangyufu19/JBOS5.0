//package com.jbosframework.context.support;
//import com.jbosframework.aop.aspectj.support.AspectMetadata;
//import com.jbosframework.context.ApplicationContext;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import java.util.Collections;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * AspectProxyContext
// * @author youfu.wang
// * @version 1.0
// */
//public class AspectProxyContext{
//    private static Log log= LogFactory.getLog(AspectProxyContext.class);
//    //Aspect Metadata Bean
//    protected static Map<String, AspectMetadata> metadataMap= Collections.synchronizedMap(new LinkedHashMap<String, AspectMetadata>());
//    private ApplicationContext applicationContext;
//
//    private boolean enableAspectJAutoProxy=false;
//
//    public AspectProxyContext(ApplicationContext applicationContext){
//        this.applicationContext=applicationContext;
//    }
//    public ApplicationContext getApplicationContext(){
//        return this.applicationContext;
//    }
//    /**
//     * 启用切面
//     * @param enableAspectJAutoProxy
//     */
//    public void enableAspectJAutoProxy(boolean enableAspectJAutoProxy){
//        this.enableAspectJAutoProxy=enableAspectJAutoProxy;
//        if(this.enableAspectJAutoProxy){
//            applicationContext.addBeanRegistry(new AspectProxyRegistry(this));
//            AnnotationAspectjProcessor annotationAspectjProcessor=new AnnotationAspectjProcessor(this);
//            annotationAspectjProcessor.setOrder(20);
//            applicationContext.addBeanPostProcessor(annotationAspectjProcessor);
//        }
//    }
//
//    /**
//     * 是否启用切面
//     * @return
//     */
//    public boolean isEnableAspectJAutoProxy(){
//        return this.enableAspectJAutoProxy;
//    }
//
//
//    public boolean contains(String pointcut){
//        if(pointcut==null){
//            return false;
//        }
//        if(metadataMap.containsKey(pointcut)){
//            return true;
//        }
//        return false;
//    }
//    public void putMetadata(AspectMetadata metadata){
//        if(!metadataMap.containsKey(metadata.getPointcut())){
//            metadataMap.put(metadata.getPointcut(),metadata);
//        }
//    }
//    public AspectMetadata getMetadata(String pointcut){
//        if(metadataMap.containsKey(pointcut)){
//            return metadataMap.get(pointcut);
//        }
//        return null;
//    }
//    public Map<String, AspectMetadata> getMetadataMap(){
//        return metadataMap;
//    }
//}
