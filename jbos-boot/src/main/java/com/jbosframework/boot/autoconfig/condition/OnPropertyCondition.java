package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AbstractCondition;
import java.lang.annotation.Annotation;

/**
 * OnPropertyCondition
 * @author youfu.wang
 */
public class OnPropertyCondition extends AbstractCondition {
    private Annotation annotation;

    public OnPropertyCondition(ConfigurableApplicationContext applicationContext, Annotation annotation){
        super(applicationContext);
        this.annotation=annotation;
    }

    public boolean matches() {
        if(this.annotation==null){
            return false;
        }
        ConditionalOnProperty conditionalOnProperty=(ConditionalOnProperty)annotation;
        if(this.getApplicationContext().getEnvironment().getProperty(conditionalOnProperty.name()).equals(conditionalOnProperty.value())){
            return true;
        }
        return false;
    }
}
