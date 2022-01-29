//package com.jbosframework.context.support;
//
//import com.jbosframework.aop.AopProxy;
//import com.jbosframework.aop.AdviceConfig;
//import com.jbosframework.aop.CglibProxy;
//import com.jbosframework.aop.aspectj.support.AspectMetadata;
//import com.jbosframework.beans.config.BeanPostProcessor;
//import com.jbosframework.core.Order;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import java.lang.reflect.Method;
//
///**
// * AnnotationAspectjProcessor
// * @author youfu.wang
// * @version 5.0
// */
//public class AnnotationAspectjProcessor implements BeanPostProcessor {
//    private static final Log log= LogFactory.getLog(AnnotationAspectjProcessor.class);
//    private AspectProxyContext aspectProxyContext;
//    private int order= Order.MIN;
//
//    public AnnotationAspectjProcessor(AspectProxyContext aspectProxyContext){
//        this.aspectProxyContext=aspectProxyContext;
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
//    public Object process(Object obj){
//        Object target=obj;
//        PointcutMethodMatcher pointcutMethodMatcher=new PointcutMethodMatcher(this.aspectProxyContext);
//        if(pointcutMethodMatcher.match(target)){
//            //判断是否切面AOP代理Bean
//            target=pointcutMethodMatcher.getAspectAopProxy(target);
//        }
//        return target;
//    }
//    /**
//     * PointcutMethodMatcher
//     * @author youfu.wang
//     * @version 5.0
//     */
//    public class PointcutMethodMatcher {
//        private AspectProxyContext aspectProxyContext;
//        private AspectMetadata aspectMetadata;
//
//        /**
//         * 构造方法
//         *
//         * @param aspectProxyContext
//         */
//        public PointcutMethodMatcher(AspectProxyContext aspectProxyContext) {
//            this.aspectProxyContext = aspectProxyContext;
//        }
//
//        /**
//         * 捕获Bean对象切入点方法
//         *
//         * @return
//         */
//        public boolean match(Object obj) {
//            boolean bool = false;
//            if (obj == null) {
//                return false;
//            }
//            Method[] methods = obj.getClass().getDeclaredMethods();
//            if (methods == null) {
//                return false;
//            }
//            for (Method method : methods) {
//                String pointcut = obj.getClass().getName() + "." + method.getName();
//                if (this.aspectProxyContext.contains(pointcut)) {
//                    aspectMetadata = this.aspectProxyContext.getMetadata(pointcut);
//                    aspectMetadata.getAdviceConfig().getMethodAdvisor().setTarget(obj);
//                    aspectMetadata.getAdviceConfig().getMethodAdvisor().setAdviceMethod(method.getName());
//                    bool = true;
//                    break;
//                }
//            }
//            return bool;
//        }
//
//        /**
//         * 得到AOP代理对象
//         * @param obj
//         * @return
//         */
//        public Object getAspectAopProxy(Object obj) {
//            AdviceConfig adviceConfig = aspectMetadata.getAdviceConfig();
//            adviceConfig.setTarget(obj);
//            AopProxy aopProxy = new CglibProxy(adviceConfig);
//            return aopProxy.getProxy();
//        }
//    }
//}
