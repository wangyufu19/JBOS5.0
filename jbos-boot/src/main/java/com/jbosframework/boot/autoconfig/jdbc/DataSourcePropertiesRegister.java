package com.jbosframework.boot.autoconfig.jdbc;

import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.ImportSelector;
import java.util.List;

public class DataSourcePropertiesRegister implements ImportSelector {
    private ConfigurableApplicationContext applicationContext;

    public DataSourcePropertiesRegister(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public List<String> processImports() {
        GenericBeanDefinition genericBeanDefinition=new GenericBeanDefinition(DataSourcePropertiesBeanPostProcessor.class);
        this.applicationContext.getBeanFactory().putBeanDefinition(genericBeanDefinition.getName(),genericBeanDefinition);
        return null;
    }
}
