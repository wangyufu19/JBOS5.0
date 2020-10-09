package com.jbosframework.context.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Configuration
 * @author youfu.wang
 * @version 1.0
 */
public class Configuration {
    private static Log log= LogFactory.getLog(Configuration.class);
    private String configLocation;
    private YamlConfig yamlConfig=new YamlConfig();
    private Environment environment=new Environment();
    private boolean enableAspectJAutoProxy=false;
    private boolean enableAutoConfiguration=false;


    /**
     * 构造方法
     */
    public Configuration(){
        configLocation="jbosContext.yml";
    }

    /**
     * 构造方法
     * @param configLocation
     */
    public Configuration(String configLocation){
        this.configLocation=configLocation;
    }
    /**
     * 加载配置属性
     */
    public void load() {
        yamlConfig.load(configLocation);
    }
    /**
     * 设置上下文环境
     */
    public void setEnvironment(Environment environment){
        this.environment=environment;
    }
    /**
     * 得到上下文环境
     * @return
     */
    public Environment getEnvironment() {
        return this.environment;
    }

    /**
     * 设置启用切面
     * @param enableAspectJAutoProxy
     */
    public void setEnableAspectJAutoProxy(boolean enableAspectJAutoProxy){
        this.enableAspectJAutoProxy=enableAspectJAutoProxy;
    }

    /**
     * 得到启用切面
     * @return
     */
    public boolean getEnableAspectJAutoProxy(){
        return this.enableAspectJAutoProxy;
    }

    /**
     * 设置启用自动配置
     * @param enableAutoConfiguration
     */
    public void setEnableAutoConfiguration(boolean enableAutoConfiguration){
        this.enableAutoConfiguration=enableAutoConfiguration;
    }

    /**
     * 得到启用自动配置
     * @return
     */
    public boolean getEnableAutoConfiguration(){
        return this.enableAutoConfiguration;
    }
    /**
     * 注入上下文属性
     * @param name
     * @param value
     */
    public void putContextProperty(String name,String value){

    }

    /**
     * 得到上下文属性
     * @param name
     * @return
     */
    public Object getContextProperty(String name){
        Object value=null;
        value=yamlConfig.getValue(name);
        return value;
    }

    /**
     * 清除属性内存
     */
    public void clear(){
        yamlConfig.clear();
    }
}
