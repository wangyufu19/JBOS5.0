package com.jbosframework.context.annotation;

import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Value;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
import com.jbosframework.core.jepl.JEPL;
import com.jbosframework.utils.JBOSClassloader;
import com.jbosframework.utils.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AutowiredAnnotationBeanPostProcessor
 * @author youfu.wang
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered {
    private static final Log logger= LogFactory.getLog(AutowiredAnnotationBeanPostProcessor.class);
    private int order=Ordered.HIGHEST_PRECEDENCE+20;

    private ConfigurableApplicationContext applicationContext;

    public AutowiredAnnotationBeanPostProcessor(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition) {
        Field[] fields=bean.getClass().getDeclaredFields();
        if(fields==null){
            return bean;
        }
        for(Field field:fields){
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                this.injectAutowiredAnnotation(bean,field);
                this.injectValueAnnotation(bean,field);
            }
        }
        return bean;
    }
    private void injectAutowiredAnnotation(Object bean,Field field){
        Object fieldValue=null;
        Autowired autowired=field.getDeclaredAnnotation(Autowired.class);
        if(autowired==null){
            return;
        }
        if(field.getType().getName().equals(List.class.getName())){
            ParameterizedType type=(ParameterizedType)field.getGenericType();
            if(null!=type.getActualTypeArguments()&&type.getActualTypeArguments().length>0){
                try {
                    List<String> list=Arrays.asList(this.applicationContext.getBeanNamesOfType(JBOSClassloader.loadClass(type.getActualTypeArguments()[0].getTypeName())));
                    if(null!=list){
                        List valueList=new ArrayList();
                        for(int i=0;i<list.size();i++){
                            valueList.add(this.applicationContext.getBean(list.get(i)));
                        }
                        fieldValue=valueList;
                    }
                } catch (ClassNotFoundException e) {
                    logger.error(e.getMessage());
                }
            }
        }else{
            fieldValue=this.applicationContext.getBean(field.getType().getName());
        }
        InjectionMetadata.inject(bean,field,fieldValue);
    }
    private void injectValueAnnotation(Object bean,Field field){
        String propertyName;
        Object fieldValue;
        Value value=field.getDeclaredAnnotation(Value.class);
        if(value!=null){
            propertyName=value.value();
            if(JEPL.matches(propertyName)){
                propertyName=propertyName.substring(propertyName.indexOf(JEPL.JEPL_PATTERN_PREFIX)+2);
                propertyName=propertyName.substring(0,propertyName.indexOf(JEPL.JEPL_PATTERN_SUFFIX));
            }
            fieldValue=this.applicationContext.getEnvironment().getProperty(propertyName);
            InjectionMetadata.inject(bean,field,TypeConverter.convert(field.getType().getName(),fieldValue));
        }
    }
    public int getOrder() {
        return order;
    }
}
