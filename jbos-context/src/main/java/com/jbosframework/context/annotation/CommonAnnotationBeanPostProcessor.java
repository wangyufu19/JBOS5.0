package com.jbosframework.context.annotation;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
import com.jbosframework.utils.JBOSClassCaller;
import com.jbosframework.utils.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * CommonAnnotationBeanPostProcessor
 * @author youfu.wang
 */
public class CommonAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered {
    private static final Log logger= LogFactory.getLog(CommonAnnotationBeanPostProcessor.class);
    private int order=Ordered.HIGHEST_PRECEDENCE+21;

    private ConfigurableApplicationContext applicationContext;

    public CommonAnnotationBeanPostProcessor(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition){
        Method[] methods=bean.getClass().getDeclaredMethods();
        if(ObjectUtils.isEmpty(methods)) {
           return bean;
        }
        for (Method method : methods) {
            Annotation[] annotations=method.getDeclaredAnnotations();
            //Init
            //PostConstruct
            if(this.isComponent(annotations, PostConstruct.class)){
                MethodMetadata methodMetadata=MethodMetadata.createMethodMetadata(method);
                this.invokeMethod(bean,methodMetadata);
            }
            //Destroy
        }
        return bean;
    }
    private boolean isComponent(Annotation[] annotations,Class<?> cls){
        if(annotations==null){
            return false;
        }
        for(Annotation annotation:annotations){
            if(annotation.annotationType().getName().equals(cls.getName())){
                return true;
            }
        }
        return false;
    }
    private Object invokeMethod(Object parentBean,MethodMetadata methodMetadata){
        if(methodMetadata.getMethodParameters().length>0){
            Object[] parameterValues=new Object[methodMetadata.getMethodParameters().length];
            for(int i=0;i<methodMetadata.getMethodParameters().length;i++){
                Object refObj=applicationContext.getBean(methodMetadata.getMethodParameters()[i].getType().getName());
                parameterValues[i]=refObj;
            }
            return JBOSClassCaller.call(parentBean,methodMetadata.getMethod(),parameterValues,methodMetadata.getParameterTypes());
        }else{
            return JBOSClassCaller.call(parentBean,methodMetadata.getMethod());
        }
    }
}
