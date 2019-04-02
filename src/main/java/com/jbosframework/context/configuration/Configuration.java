package com.jbosframework.context.configuration;
import com.jbosframework.core.io.ClassPathResource;
import com.jbosframework.core.io.Resource;
import com.jbosframework.core.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuration
 * @author youfu.wang
 * @version 1.0
 */
public class Configuration {
    private static Log log= LogFactory.getLog(Configuration.class);
    private static String defaultConfigLocation;
    private String configLocation;
    protected static Map<String,String> contextProperties= Collections.synchronizedMap(new LinkedHashMap<String,String>());
    private Environment environment=new Environment();
    private boolean enableAspectJAutoProxy=false;

    static{
        defaultConfigLocation="application.properties";
    }

    /**
     * 构造方法
     */
    public Configuration(){
        configLocation=defaultConfigLocation;
    }

    /**
     * 构造方法
     * @param configLocation
     */
    public Configuration(String configLocation){
        this.configLocation=configLocation;
    }
    /**
     * 初始化资源
     */
    public void init(){
        Resource resource=new ClassPathResource(configLocation);
        Properties pros=new Properties();
        try {
            pros.load(resource.getInputStream());
            for(Map.Entry<Object,Object> pro:pros.entrySet()){
                String name= StringUtils.replaceNull(pro.getKey());
                String value=StringUtils.replaceNull(pro.getValue());
                if(!"".equals(value)){
                    this.putContextProperty(name,value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置上下文环境
     * @return
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
     * 注入上下文属性
     * @param name
     * @param value
     */
    public void putContextProperty(String name,String value){
        contextProperties.put(name,value);
    }

    /**
     * 得到上下文属性
     * @param name
     * @return
     */
    public String getContextProperty(String name){
        return contextProperties.get(name);
    }

    /**
     * 得到上下文属性
     * @return
     */
    public Map<String,String> getContextProperties(){
        return contextProperties;
    }
}
