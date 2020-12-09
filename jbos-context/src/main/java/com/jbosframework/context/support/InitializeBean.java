package com.jbosframework.context.support;

import com.jbosframework.beans.annotation.Value;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.core.jepl.JEPL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * InitializeBean
 * @author youfu.wang
 * @version 5.0
 */
public class InitializeBean {
    private static final Log log= LogFactory.getLog(InitializeBean.class);
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    public ApplicationContext getApplicationContext(){
        return this.applicationContext;
    }
    public void afterProperties(Object obj){
        Class<?> cls=null;
        if (obj==null){
            return;
        }
        cls=obj.getClass();
        Field[] fields=cls.getDeclaredFields();
        if(fields==null) {
            return;
        }
        InjectionMetadata injectionMetadata=new InjectionMetadata(this.applicationContext);
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
                    fieldValue=this.applicationContext.getContextConfiguration().getContextProperty(s1);
                    injectionMetadata.inject(obj,fields[i],fieldValue);
                }
            }
        }
    }
}