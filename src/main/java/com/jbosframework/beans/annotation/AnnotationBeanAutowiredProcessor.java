package com.jbosframework.beans.annotation;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.factory.BeanTypeException;
import com.jbosframework.core.jepl.JEPL;
import com.jbosframework.orm.mybatis.SqlSessionBeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.annotation.Annotation;
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
public class AnnotationBeanAutowiredProcessor implements BeanPostProcessor {
    private static final Log log= LogFactory.getLog(AnnotationBeanAutowiredProcessor.class);
    private BeanFactory beanFactory;

    public AnnotationBeanAutowiredProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    public Object process(Object bean){
        return this.process(bean,null);
    }
    public Object process(Object bean,BeanDefinition beanDefinition){
        Object obj=bean;
        Class<?> cls=null;
        if (obj==null){
            return obj;
        }
        cls=obj.getClass();
        Field[] fields=cls.getDeclaredFields();
        if(fields==null) {
            return obj;
        }
        InjectionMetadata injectionMetadata=new InjectionMetadata(this.beanFactory);
        for(int i=0;i<fields.length;i++) {
            Annotation[] annotations = fields[i].getAnnotations();
            if (annotations == null || annotations.length <= 0) {
                continue;
            }
            //校验字段注解是否用在了static方法上
            if (Modifier.isStatic(fields[i].getModifiers())) {
                if (log.isWarnEnabled()) {
                    log.warn("Field annotation is not supported on static fields: " + fields[i].getName());
                }
                return obj;
            }
            for(int j=0;j<annotations.length;j++) {
                //装配Bean对象字段值
                Object fieldValue=this.autowire(fields[i],annotations[j]);
                injectionMetadata.inject(obj,fields[i],fieldValue);
            }
        }
        return obj;
    }
    /**
     * 装配Bean对象字段值
     * @param field
     * @param annotation
     */
    private Object autowire(Field field, Annotation annotation){
        Object fieldValue=null;
        if(annotation instanceof Autowired) {
            if(field.getType().isInterface()){
                Map<String, BeanDefinition> beansTypesMap=this.beanFactory.getBeanNamesOfType(field.getType());
                List<BeanDefinition> beanNames=new ArrayList<BeanDefinition>();
                String beanNameClass="";
                for(Map.Entry<String,BeanDefinition> entry:beansTypesMap.entrySet()){
                    beanNameClass+=entry.getValue().getName()+",";
                    beanNames.add(entry.getValue());
                }
                if(beanNames.size()<=0){
                    fieldValue=this.beanFactory.getBean(field.getType().getName());
                }else{
                    if(beanNames.size()>1){
                        BeanTypeException ex = new BeanTypeException("指定的类型Bean'" + field.getType() + "' available:找到多个实现Bean["+beanNameClass+"]");
                        ex.printStackTrace();
                        return null;
                    }else{
                        fieldValue=this.beanFactory.getBean(beanNames.get(0).getName());
                    }
                }
            }else{
                fieldValue=this.beanFactory.getBean(field.getType().getName());
            }
        }else if(annotation instanceof Value) {
            String s1;
            Value valueAnnotation=(Value)annotation;
            s1=valueAnnotation.value();
            //判断值引用JEPL表达式的值
            if(JEPL.matches(s1)){
                s1=s1.replace(JEPL.JEPL_PATTERN_PREFIX, "").replace(JEPL.JEPL_PATTERN_SUFFIX, "");
                fieldValue=this.beanFactory.getContextConfiguration().getContextProperty(s1);
            }
        }else if(annotation instanceof Mapper){
            if(field.getType().isInterface()) {
                SqlSessionFactory sqlSessionFactory=(SqlSessionFactory)this.beanFactory.getBean(SqlSessionFactory.class.getName());
                if(SqlSessionBeanUtils.isMapperBean(sqlSessionFactory,field.getType())){
                    MapperProxyFactory mapperProxyFactory=new MapperProxyFactory(field.getType());
                    fieldValue=mapperProxyFactory.newInstance(sqlSessionFactory.openSession());
                }
            }
        }
        return fieldValue;
    }
}
