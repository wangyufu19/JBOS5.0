package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AbstractCondition;
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

    public OnBeanCondition(ConfigurableApplicationContext applicationContext, Annotation annotation){
        super(applicationContext);
        this.annotation=annotation;
    }
    public boolean matches() {
        Class<?>[] classes=null;
        boolean match=true;
        if(this.annotation==null){
            return false;
        }

        if(this.annotation instanceof ConditionalOnBean){
            ConditionalOnBean conditionalOnBean=((ConditionalOnBean)this.annotation);
            classes=conditionalOnBean.value();
            match=conditionalOnBean.match();
        }else if(this.annotation instanceof ConditionalOnMissingBean){
            ConditionalOnMissingBean conditionalOnMissingBean=((ConditionalOnMissingBean)this.annotation);
            classes=conditionalOnMissingBean.value();
            match=conditionalOnMissingBean.match();
        }
        if(classes!=null){
            for(Class<?> cls:classes){
                if(this.getApplicationContext().getBeanFactory().containsBean(cls.getName())==match){
                    return true;
                }
            }
        }
        return false;
    }
}
