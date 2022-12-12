package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.context.annotation.AbstractCondition;
import com.jbosframework.core.env.ConfigurableEnvironment;

import java.lang.annotation.Annotation;

/**
 * OnPropertyCondition
 * @author youfu.wang
 */
public class OnPropertyCondition extends AbstractCondition {
    private Annotation annotation;

    public OnPropertyCondition(ConfigurableEnvironment environment, BeanDefinitionRegistry registry, Annotation annotation){
        super(environment,registry);
        this.annotation=annotation;
    }

    public boolean matches() {
        if(this.annotation==null){
            return false;
        }
        ConditionalOnProperty conditionalOnProperty=(ConditionalOnProperty)annotation;
        if(this.getEnvironment().getProperty(conditionalOnProperty.name()).equals(conditionalOnProperty.value())){
            return true;
        }
        return false;
    }
}
