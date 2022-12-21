package com.jbosframework.schedule.annotation.async;

import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.ImportSelector;

import java.util.List;

public class AsyncRegister implements ImportSelector{
    private ConfigurableApplicationContext applicationContext;

    public AsyncRegister(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public List<String> processImports() {
        AsyncBeanPostProcessor asyncBeanPostProcessor=new AsyncBeanPostProcessor(this.applicationContext);
        this.applicationContext.getBeanFactory().registerBeanPostProcessor(asyncBeanPostProcessor);
        return null;
    }
}
