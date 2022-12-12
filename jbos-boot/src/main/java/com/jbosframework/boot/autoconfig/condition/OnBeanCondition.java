package com.jbosframework.boot.autoconfig.condition;
import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.context.annotation.AbstractCondition;
import com.jbosframework.core.env.ConfigurableEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;

/**
 * OnBeanCondition
 * @author youfu.wang
 */
public class OnBeanCondition extends AbstractCondition {
    private static final Log logger= LogFactory.getLog(OnBeanCondition.class);
    private Annotation annotation;

    public OnBeanCondition(ConfigurableEnvironment environment, BeanDefinitionRegistry registry, Annotation annotation){
        super(environment,registry);
        this.annotation=annotation;
    }
    public boolean matches() {
        boolean bool=false;
        if(this.annotation==null){
            return false;
        }
        Class<?>[] classes=((ConditionalOnBean)this.annotation).value();
        if(classes!=null){
            for(Class<?> cls:classes){
                bool= this.getRegistry().containsBean(cls.getName());
            }
        }
        return bool;
    }
}
