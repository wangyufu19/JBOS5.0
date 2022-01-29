package com.jbosframework.context.support;

import com.jbosframework.beans.config.*;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.factory.InitializingBean;
import com.jbosframework.utils.JBOSClassCaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AbstractFactoryBean
 * @author youfu.wang
 * @version 5.0
 */
public abstract class AbstractFactoryBean implements InitializingBean,BeanFactory {
    private static final Log log= LogFactory.getLog(AbstractFactoryBean.class);


    public Object afterPropertiesSet(Object obj){
       return null;
    }
    /**
     * 创建方法Bean
     * @param beanDefinition
     * @return
     */
    public Object doCreateMethodBean(BeanDefinition beanDefinition){
        Object obj=null;
        Object parentObj=this.getBean(beanDefinition.getParentName());
        DefaultBeanDefinition defaultBeanDefinition=(DefaultBeanDefinition)beanDefinition;
        MethodMetadata methodMetadata=defaultBeanDefinition.getMethodMetadata();
        if(methodMetadata.getMethodParameters().length>0){
            Object[] parameterValues=new Object[methodMetadata.getMethodParameters().length];
            for(int i=0;i<methodMetadata.getMethodParameters().length;i++){
                Object refObj=this.getBean(methodMetadata.getMethodParameters()[i].getType().getName());
                parameterValues[i]=refObj;
            }
            obj=JBOSClassCaller.call(parentObj,methodMetadata.getMethodName(),parameterValues,methodMetadata.getParameterTypes());
        }else{
            obj=JBOSClassCaller.call(parentObj,methodMetadata.getMethodName());
        }
        return obj;
    }

    /**
     * 调用Bean初始化方法
     * @param beanDefinition
     */
    public void invokeBeanMethod(Object obj,BeanDefinition beanDefinition){
        String initMethod=beanDefinition.getInitMethod();
        if(initMethod!=null&&!"".equals(initMethod)){
            JBOSClassCaller.call(obj,initMethod);
        }
    }





}
