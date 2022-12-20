package com.jbosframework.boot.autoconfig.orm.mybatis;

import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.annotation.ImportSelector;

import java.util.List;

public class MybatisMapperRegister implements ImportSelector {
    private ConfigurableApplicationContext applicationContext;

    public MybatisMapperRegister(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public List<String> processImports() {
        MapperBeanPostProcessor mapperBeanPostProcessor=new MapperBeanPostProcessor();
        this.applicationContext.getBeanFactory().putBeanDefinition(
                mapperBeanPostProcessor.getClass().getName(),
                new GenericBeanDefinition(mapperBeanPostProcessor.getClass())
        );
        return null;
    }
}
