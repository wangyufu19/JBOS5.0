package com.jbosframework.context.annotation;

import com.jbosframework.context.ConfigurableApplicationContext;

public abstract class AbstractCondition implements Condition{
    private ConfigurableApplicationContext applicationContext;

    public AbstractCondition(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public abstract boolean matches();


}
