package com.jbosframework.boot.autoconfig;

import com.jbosframework.beans.factory.BeanRegistryFactory;
import com.jbosframework.beans.support.BeanRegistry;
import com.jbosframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoConfigurationSelector
 * @author youfu.wang
 * @version 1.0
 */
public class AutoConfigurationSelector {
    private List<String> autConfigurationBeans=new ArrayList<String>();

    /**
     * 构造方法
     */
    public AutoConfigurationSelector(){
        autConfigurationBeans.add("com.jbosframework.boot.autoconfig.jdbc.DataSourceAutoConfiguration");
    }

    /**
     * 注册自动配置组件到容器中
     * @param ctx
     */
    public void registry(ApplicationContext ctx){
        BeanRegistry registry= BeanRegistryFactory.getBeanRegistry();
        for (String autoCls:autConfigurationBeans){

        }
        //registry.registerBean();
    }
}
