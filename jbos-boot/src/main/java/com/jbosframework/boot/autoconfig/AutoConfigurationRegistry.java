package com.jbosframework.boot.autoconfig;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.core.io.ClassPathResource;
import com.jbosframework.core.io.Resource;
import com.jbosframework.utils.JBOSClassloader;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * AutoConfigurationRegistry
 * @author youfu.wang
 * @version 5.0
 */
public class AutoConfigurationRegistry {
    private static final String com$jbosframework$boot$autoconfig$EnableAutoConfiguration="com.jbosframework.boot.autoconfig.EnableAutoConfiguration";

    private static String[] autConfigurationBeans;

    private String autoConfigLocation;
    /**
     * 构造方法
     */
    public AutoConfigurationRegistry(){
        autoConfigLocation="META-INF"+ File.separator+"jbos-autoconfigure-metadata.properties";
        this.loadAutoConfigurationBeans();
    }

    /**
     * 加载自动配置类
     */
    private void loadAutoConfigurationBeans(){
        Resource resource=new ClassPathResource(autoConfigLocation);
        Properties pros=new Properties();
        try {
            pros.load(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(pros==null){
            return;
        }
        String enableAutoConfiguration=pros.getProperty(AutoConfigurationRegistry.com$jbosframework$boot$autoconfig$EnableAutoConfiguration);
        if(enableAutoConfiguration!=null){
            autConfigurationBeans=enableAutoConfiguration.split(",");
        }
    }
    /**
     * 注册自动配置组件到容器中
     * @param ctx
     */
    public void registry(ApplicationContext ctx)  {
        try{
            for (String s:autConfigurationBeans){
                Class<?> autCls= JBOSClassloader.loadClass(s);
                AbstractAutoConfiguration autoConfiguration=(AbstractAutoConfiguration)JBOSClassloader.loadClass(s).newInstance();
                autoConfiguration.setApplicationContext(ctx);
                autoConfiguration.registry();
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
