package com.jbosframework.beans.annotation;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.factory.BeanTypeException;
import com.jbosframework.core.Ordered;
import com.jbosframework.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * AutowiredAnnotationBeanPostProcessor
 * @author youfu.wang
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered {
    private int order=Ordered.HIGHEST_PRECEDENCE+20;

    private BeanFactory beanFactory;

    public AutowiredAnnotationBeanPostProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition){
        Field[] fields=bean.getClass().getDeclaredFields();
        if(fields==null){
            return bean;
        }
        Object fieldValue=null;
        for(Field field:fields){
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                Autowired autowired=field.getDeclaredAnnotation(Autowired.class);
                if(autowired==null){
                    continue;
                }
                if(field.getType().isInterface()){
                    String[] beanNames=this.beanFactory.getBeanNamesOfType(field.getType());
                    if(beanNames.length<=0){
                        fieldValue=this.beanFactory.getBean(field.getType().getName());
                    }else{
                        if(beanNames.length>1){
                            BeanTypeException ex = new BeanTypeException("指定的类型Bean'" +field.getType() + "' available:找到多个实现Bean["+ StringUtils.stringArrayToTokenize(beanNames,",") +"]");
                            ex.printStackTrace();
                            return bean;
                        }else{
                            fieldValue=this.beanFactory.getBean(beanNames[0]);
                        }
                    }
                }else{
                    fieldValue=this.beanFactory.getBean(field.getType().getName());
                }
                InjectionMetadata.inject(bean,field,fieldValue);
            }
        }
        return bean;
    }
}
