package com.jbosframework.boot;

import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.context.annotation.AnnotatedBeanDefinitionReader;
import com.jbosframework.core.annotaion.AnnotationUtils;


public class BeanDefinitionLoader {

    private final AnnotatedBeanDefinitionReader reader;

    private final Object[] sources;

    public BeanDefinitionLoader(BeanDefinitionRegistry registry, Object... sources){
        this.reader= new AnnotatedBeanDefinitionReader(registry);
        this.sources=sources;
    }
    public int load(Class annotationType){
        int count = 0;
        for (Object source : this.sources) {
            count += load(source,annotationType);
        }
        return count;
    }
    public int load(Object source,Class annotationType){
        if(AnnotationUtils.isComponent((Class<?>)source,annotationType)){
            this.reader.registryBean((Class<?>)source);
            return 1;
        }else{
            return 0;
        }
    }
}
