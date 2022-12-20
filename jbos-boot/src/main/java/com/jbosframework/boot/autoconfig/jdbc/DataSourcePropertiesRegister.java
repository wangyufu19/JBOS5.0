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
        DataSourcePropertiesBeanPostProcessor dataSourcePropertiesBeanPostProcessor=new DataSourcePropertiesBeanPostProcessor();
        this.applicationContext.getBeanFactory().putBeanDefinition(
                dataSourcePropertiesBeanPostProcessor.getClass().getName(),
                new GenericBeanDefinition(dataSourcePropertiesBeanPostProcessor.getClass())
        );
        return null;
    }
}
