package com.jbosframework.context.annotation;

import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.core.env.ConfigurableEnvironment;

public abstract class AbstractCondition implements Condition{
    private ConfigurableEnvironment environment;
    private BeanDefinitionRegistry registry;

    public AbstractCondition(ConfigurableEnvironment environment,BeanDefinitionRegistry registry){
        this.environment=environment;
        this.registry=registry;
    }

    public ConfigurableEnvironment getEnvironment() {
        return environment;
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    public abstract boolean matches();


}
