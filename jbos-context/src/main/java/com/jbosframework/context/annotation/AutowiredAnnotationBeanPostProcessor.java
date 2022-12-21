package com.jbosframework.context.annotation;

import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Value;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
import com.jbosframework.core.jepl.JEPL;
import com.jbosframework.utils.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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

    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition){
        Field[] fields=bean.getClass().getDeclaredFields();
        if(fields==null){
            return bean;
        }
        String propertyName;
        Object fieldValue=null;
        for(Field field:fields){
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                Autowired autowired=field.getDeclaredAnnotation(Autowired.class);
                Value value=field.getDeclaredAnnotation(Value.class);
                if(autowired!=null){
                    fieldValue=this.applicationContext.getBean(field.getType().getName());
                    InjectionMetadata.inject(bean,field,fieldValue);
                }
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
        }
        return bean;
    }

    public int getOrder() {
        return order;
    }
}
