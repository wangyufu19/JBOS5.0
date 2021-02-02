package com.jbosframework.context.support;

import com.jbosframework.beans.annotation.Value;
import com.jbosframework.beans.config.*;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.factory.BeanInstanceUtils;
import com.jbosframework.beans.factory.InitializingBean;
import com.jbosframework.context.configuration.Configuration;
import com.jbosframework.core.jepl.JEPL;
import com.jbosframework.utils.JBOSClassCaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * AbstractFactoryBean
 * @author youfu.wang
 * @version 5.0
 */
public abstract class AbstractFactoryBean implements InitializingBean,BeanFactory {
    private static final Log log= LogFactory.getLog(AbstractFactoryBean.class);

    private static List<BeanBeforeProcessor> beanBeforeProcessors= Collections.synchronizedList(new ArrayList<BeanBeforeProcessor>());

    private static List<BeanPostProcessor> beanPostProcessors=Collections.synchronizedList(new LinkedList<BeanPostProcessor>());
    /**
     * 得到上下文配置
     * @return
     */
    public abstract Configuration getContextConfiguration();

    public void afterPropertiesSet(Object obj){
        Class<?> cls=null;
        if (obj==null){
            return;
        }
        cls=obj.getClass();
        Field[] fields=cls.getDeclaredFields();
        if(fields==null) {
            return;
        }
        InjectionMetadata injectionMetadata=new InjectionMetadata(this);
        for(int i=0;i<fields.length;i++) {
            Value valueAnnotation=fields[i].getDeclaredAnnotation(Value.class);
            if(valueAnnotation!=null){
                //校验字段注解是否用在了static方法上
                if (Modifier.isStatic(fields[i].getModifiers())) {
                    if (log.isWarnEnabled()) {
                        log.warn("Field com.jbosframework.beans.annotation is not supported on static fields: " + fields[i].getName());
                    }
                    return;
                }
                Object fieldValue=null;
                String s1;
                s1=valueAnnotation.value();
                //判断值引用JEPL表达式的值
                if(JEPL.matches(s1)){
                    s1=s1.replace(JEPL.JEPL_PATTERN_PREFIX, "").replace(JEPL.JEPL_PATTERN_SUFFIX, "");
                    fieldValue=this.getContextConfiguration().getContextProperty(s1);
                    injectionMetadata.inject(obj,fields[i],fieldValue);
                }
            }
        }
    }
    /**
     * 创建方法Bean
     * @param beanDefinition
     * @return
     */
    public Object doCreateMethodBean(BeanDefinition beanDefinition){
        Object obj=null;
        Object parentObj=this.getBean(beanDefinition.getParentName());
        MethodMetadata methodMetadata=beanDefinition.getMethodMetadata();
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
    /**
     * 添加BeanBeforeProcessor
     * @param beanBeforeProcessor
     */
    public void addBeanBeforeProcessor(BeanBeforeProcessor beanBeforeProcessor){
        beanBeforeProcessors.add(beanBeforeProcessor);
    }
    /**
     * 添加BeanPostProcessor
     * @param beanPostProcessor
     */
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        beanPostProcessors.add(beanPostProcessor);
        Collections.sort(this.getBeanPostProcessors(), new Comparator<BeanPostProcessor>() {
            @Override
            public int compare(BeanPostProcessor o1, BeanPostProcessor o2) {
                return o1.getOrder()-o2.getOrder();
            }
        });
    }
    public List<BeanBeforeProcessor> getBeanBeforeProcessors(){
        return beanBeforeProcessors;
    }
    public List<BeanPostProcessor> getBeanPostProcessors(){
        return beanPostProcessors;
    }

    /**
     * 处理Bean对象的Processor
     * @param bean
     * @param beanDefinition
     * @return
     */
    public void doBeanBeforeProcessor(Object bean,BeanDefinition beanDefinition){
        for(BeanBeforeProcessor beanBeforeProcessor:this.getBeanBeforeProcessors()){
            beanBeforeProcessor.process(bean,beanDefinition);
        }
    }
    /**
     * 处理Bean对象的Processor
     */
    public Object doBeanPostProcessor(Object bean){
        Object obj=bean;
        if(obj==null){
            return null;
        }
        for(BeanPostProcessor beanPostProcessor:this.getBeanPostProcessors()){
            obj=beanPostProcessor.process(obj);
        }
        return obj;
    }

    public void clear(){
        beanBeforeProcessors.clear();
        beanPostProcessors.clear();
    }
}
