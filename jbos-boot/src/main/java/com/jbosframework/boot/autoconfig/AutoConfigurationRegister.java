package com.jbosframework.boot.autoconfig;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.utils.JBOSClassloader;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoConfigurationRegister
 * @author youfu.wang
 * @version 1.0
 */
public class AutoConfigurationRegister {
    private static List<String> autConfigurationBeans=new ArrayList<String>();
    /**
     * 构造方法
     */
    public AutoConfigurationRegister(){
        autConfigurationBeans.add("com.jbosframework.boot.autoconfig.jdbc.DataSourceAutoConfiguration");
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
