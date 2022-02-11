package com.jbosframework.boot.context;

import com.jbosframework.beans.annotation.Value;
import com.jbosframework.beans.factory.DependencyFactory;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.core.jepl.JEPL;

import java.lang.reflect.Field;

/**
 * DependencyFactory
 * @author youfu.wang
 * @version 1.0
 */
public class PropertyDependencyFactory implements DependencyFactory {

    private ApplicationContext applicationContext;

    public PropertyDependencyFactory(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    public void autowireDependency(Object existingBean, Field field) {
        Value valueAnnotation=field.getDeclaredAnnotation(Value.class);
        if(valueAnnotation==null){
            return;
        }
        String s1=valueAnnotation.value();
        //判断值引用JEPL表达式的值
        if(JEPL.matches(s1)){
            s1=s1.replace(JEPL.JEPL_PATTERN_PREFIX, "").replace(JEPL.JEPL_PATTERN_SUFFIX, "");
            this.inject(existingBean,field,this.applicationContext.getPropertyValue(s1));
        }
    }
}
