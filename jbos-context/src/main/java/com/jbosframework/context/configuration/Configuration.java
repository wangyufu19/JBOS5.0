package com.jbosframework.context.configuration;

import com.jbosframework.core.io.ClassPathResource;
import com.jbosframework.core.io.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;


/**
 * Configuration
 * @author youfu.wang
 * @version 1.0
 */
public class Configuration {
    private static Log log= LogFactory.getLog(Configuration.class);
    private String configLocation;
    private String configClassPath;
    private YamlConfig yamlConfig=new YamlConfig();
    private Environment environment=new Environment();


    public static final String CTX_PROPERTY_PROFILES_ACTIVE="jbos.profiles.active";

    /**
     * 构造方法
     */
    public Configuration(){
        configLocation="jbosContext";
        this.load();
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
    private void load() {
        yamlConfig.setEnvironment(environment);
        Resource resource=new ClassPathResource(configLocation+".yml");
        try {
            configClassPath=resource.getFile().getParentFile().getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        yamlConfig.load(resource);
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
     * 注入上下文属性
     * @param name
     * @param value
     */
    public void putContextProperty(String name,Object value){
        yamlConfig.setValue(name,value);
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
    public String getConfigClassPath(){
        return this.configClassPath;
    }
    /**
     * 清除属性内存
     */
    public void clear(){
        yamlConfig.clear();
    }
}
