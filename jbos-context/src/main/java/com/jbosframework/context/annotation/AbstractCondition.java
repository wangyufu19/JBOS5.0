package com.jbosframework.context.annotation;

import com.jbosframework.context.ConfigurableApplicationContext;

public abstract class AbstractCondition implements Condition{
    private ConfigurableApplicationContext ctx;

    public AbstractCondition(ConfigurableApplicationContext ctx){
        this.ctx=ctx;
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return ctx;
    }

    public abstract boolean matches();


}
