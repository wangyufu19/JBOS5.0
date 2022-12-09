package com.jbosframework.boot.autoconfig.condition;

import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.AbstractCondition;

/**
 * OnPropertyCondition
 * @author youfu.wang
 */
public class OnPropertyCondition extends AbstractCondition {
    private String name;
    private String value;

    public OnPropertyCondition(ConfigurableApplicationContext ctx, String name, String value){
        super(ctx);
    }

    public boolean matches() {
        if(this.getApplicationContext().getEnvironment().getProperty(name).equals(value)){
            return true;
        }
        return false;
    }
}
