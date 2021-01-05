package com.jbosframework.beans.factory;

import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.InjectionMetadata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AnnotationBeanAutowiredProcessor
 * @author youfu.wang
 * @version 1.0
 */
public class BeanAutowiredProcessor implements BeanPostProcessor {
    private static final Log log= LogFactory.getLog(BeanAutowiredProcessor.class);
    private BeanFactory beanFactory;

    public BeanAutowiredProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    public void process(Object obj){
        Class<?> cls=null;
        if (obj==null){
            return;
        }
        cls=obj.getClass();
        Field[] fields=cls.getDeclaredFields();
        if(fields==null) {
            return;
        }
        InjectionMetadata injectionMetadata=new InjectionMetadata(this.beanFactory);
        for(int i=0;i<fields.length;i++) {
            Autowired autowiredAnnotation=fields[i].getDeclaredAnnotation(Autowired.class);
            if(autowiredAnnotation==null) {
               continue;
            }
            //校验字段注解是否用在了static方法上
            if (Modifier.isStatic(fields[i].getModifiers())) {
                if (log.isWarnEnabled()) {
                    log.warn("Field com.jbosframework.beans.annotation is not supported on static fields: " + fields[i].getName());
                }
                return;
            }
            Object fieldValue=null;
            if(fields[i].getType().isInterface()){
                Map<String, BeanDefinition> beansTypesMap=this.beanFactory.getBeanNamesOfType(fields[i].getType());
                List<BeanDefinition> beanNames=new ArrayList<BeanDefinition>();
                String beanNameClass="";
                for(Map.Entry<String,BeanDefinition> entry:beansTypesMap.entrySet()){
                    beanNameClass+=entry.getValue().getName()+",";
                    beanNames.add(entry.getValue());
                }
                if(beanNames.size()<=0){
                    fieldValue=this.beanFactory.getBean(fields[i].getType().getName());
                }else{
                    if(beanNames.size()>1){
                        BeanTypeException ex = new BeanTypeException("指定的类型Bean'" + fields[i].getType() + "' available:找到多个实现Bean["+beanNameClass+"]");
                        ex.printStackTrace();
                        return;
                    }else{
                        fieldValue=this.beanFactory.getBean(beanNames.get(0).getName());
                    }
                }
            }else{
                fieldValue=this.beanFactory.getBean(fields[i].getType().getName());
            }
            injectionMetadata.inject(obj,fields[i],fieldValue);
        }
    }
}
