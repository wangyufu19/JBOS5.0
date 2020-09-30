package com.jbosframework.beans.config;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.annotation.Mapper;
import com.jbosframework.beans.annotation.Value;
import com.jbosframework.beans.factory.BeanTypeException;
import com.jbosframework.context.support.BeanFactoryContext;
import com.jbosframework.core.jepl.JEPL;
import com.jbosframework.orm.mybatis.SqlSessionBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BeanPropertyInjection
 * @author youfu.wang
 * @version 1.0
 */
@Slf4j
public class BeanPropertyInjection {
    private BeanFactoryContext beanFactoryContext;

    /**
     * 构造方法
     * @param beanFactoryContext
     */
    public BeanPropertyInjection(BeanFactoryContext beanFactoryContext){
        this.beanFactoryContext=beanFactoryContext;
    }
    /**
     * 注入Bean对象属性
     * @param obj
     */
    public void inject(Object obj){
        Class<?> cls=null;
        cls=obj.getClass();
        Field[] fields=cls.getDeclaredFields();
        if(fields==null) {
            return;
        }
        this.putBeanField(obj,fields);
    }
    /**
     * 设置Bean字段
     * @param  obj 对象值
     * @param fields 字段属性
     */
    private void putBeanField(Object obj,Field[] fields) {

        Object fieldValue=null;

        for(int i=0;i<fields.length;i++){
            Annotation[] annotations=fields[i].getAnnotations();
            if(annotations==null||annotations.length<=0) {
                continue;
            }
            //校验字段注解是否用在了static方法上
            if (Modifier.isStatic(fields[i].getModifiers())) {
                if (log.isWarnEnabled()) {
                    log.warn("Field annotation is not supported on static fields: " + fields[i].getName());
                }
                return;
            }
            for(int j=0;j<annotations.length;j++) {
                //得到Bean字段值
                fieldValue=this.getBeanFieldValue(fields[i],annotations[j]);
            }
            //设置Bean属性值
            this.setField(obj, fields[i].getName(), fieldValue);
        }
    }

    /**
     * 得到Bean字段值
     * @param field
     * @param annotation
     * @return
     */
    private Object getBeanFieldValue(Field field,Annotation annotation){
        Object fieldValue=null;
        String s1="";
        if(annotation instanceof Autowired) {
            if(field.getType().isInterface()){
                Map<String, BeanDefinition> beansTypesMap=this.beanFactoryContext.getBeanNamesOfType(field.getType());
                List<BeanDefinition> beanNames=new ArrayList<BeanDefinition>();
                String beanNameClass="";
                for(Map.Entry<String,BeanDefinition> entry:beansTypesMap.entrySet()){
                    beanNameClass+=entry.getValue().getName()+",";
                    beanNames.add(entry.getValue());
                }
                if(beanNames.size()>1){
                    BeanTypeException ex = new BeanTypeException("指定的类型Bean'" + field.getType() + "' available:找到多个实现Bean["+beanNameClass+"]");
                    ex.printStackTrace();
                    return null;
                }
                fieldValue=this.beanFactoryContext.getBean(beanNames.get(0).getName());
            }else{
                fieldValue=this.beanFactoryContext.getBean(field.getType().getSimpleName());
            }
        }else if(annotation instanceof Value) {
            Value valueAnnotation=(Value)annotation;
            s1=valueAnnotation.value();
            //判断值引用JEPL表达式的值
            if(JEPL.matches(s1)){
                s1=s1.replace(JEPL.JEPL_PATTERN_PREFIX, "").replace(JEPL.JEPL_PATTERN_SUFFIX, "");
                fieldValue=this.beanFactoryContext.getContextConfiguration().getContextProperty(s1);
            }
        }else if(annotation instanceof Mapper){
            if(field.getType().isInterface()) {
                SqlSessionFactory sqlSessionFactory=(SqlSessionFactory)this.beanFactoryContext.getMethodBean("sqlSessionFactoryBean");
                if(SqlSessionBeanUtils.isMapperBean(sqlSessionFactory,field.getType())){
				    MapperProxyFactory mapperProxyFactory=new MapperProxyFactory(field.getType());
					fieldValue=mapperProxyFactory.newInstance(sqlSessionFactory.openSession());
				}
            }
        }
        return fieldValue;
    }
    /**
     * 设置Bean属性值
     * @param obj
     * @param name
     * @param value
     */
    private void setField(Object obj,String name,Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                return;
            }
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
