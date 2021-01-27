package com.jbosframework.context.configuration;

import com.jbosframework.core.io.ClassPathResource;
import com.jbosframework.core.io.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Configuration
 * @author youfu.wang
 * @version 1.0
 */
public class YamlConfiguration extends Configuration {
    private static Log log= LogFactory.getLog(YamlConfiguration.class);
    private String configLocation;
    private static Map<String, Object> properties= Collections.synchronizedMap(new LinkedHashMap<String,Object>());
    private Environment environment=new Environment();
    public static final String CTX_PROPERTY_PROFILES_ACTIVE="jbos.profiles.active";
    public static final String CTX_PROPERTY_CLASSPATH="jbos.classpath";
    /**
     * 构造方法
     */
    public YamlConfiguration(){
        configLocation="jbosContext";
        this.load();
    }

    /**
     * 构造方法
     * @param configLocation
     */
    public YamlConfiguration(String configLocation){
        this.configLocation=configLocation;
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
     * 加载配置属性
     */
    public void load() {
        Resource resource=new ClassPathResource(configLocation+".yml");
        this.load(resource);
        try {
            String configClassPath=resource.getFile().getParentFile().getAbsolutePath();
            this.putContextProperty(YamlConfiguration.CTX_PROPERTY_CLASSPATH,configClassPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 加载配置属性
     */
    private void load(Resource resource){
        //加载主配置
        properties=this.getConfigProperties(resource);
        String activeProfile="";
        if(this.getValue(YamlConfiguration.CTX_PROPERTY_PROFILES_ACTIVE)!=null){
            activeProfile=String.valueOf(this.getValue(YamlConfiguration.CTX_PROPERTY_PROFILES_ACTIVE));
        }
        this.environment.setActiveProfile(activeProfile);
        //加载Profile配置
        this.loadProfileConfig(activeProfile);
    }
    /**
     * 得到配置属性
     * @param resource
     */
    private Map<String, Object> getConfigProperties(Resource resource){
        Map<String, Object> configPros=new LinkedHashMap<String,Object>();
        try {
            Yaml yaml = new Yaml();
            configPros=yaml.loadAs(resource.getInputStream(), LinkedHashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configPros;
    }
    /**
     * 加载Profile配置
     * @param activeProfile
     */
    private void loadProfileConfig(String activeProfile){
        Resource resource=new ClassPathResource(configLocation+"-"+activeProfile+".yml");
        Map<String, Object> profileConfigPros=this.getConfigProperties(resource);
        if(profileConfigPros!=null){
            for(Map.Entry<String,Object> entry:profileConfigPros.entrySet()){
                if(properties.containsKey(entry.getKey())){
                    Map<String, Object> mainConfigPros=(Map<String, Object>)properties.get(entry.getKey());
                    Map<String, Object> subConfigPros=(Map<String, Object>)entry.getValue();
                    for(Map.Entry<String,Object> sub:subConfigPros.entrySet()){
                        mainConfigPros.put(sub.getKey(),sub.getValue());
                        properties.put(entry.getKey(),mainConfigPros);
                    }
                }
            }
        }
    }
    /**
     * 注入上下文属性
     * @param name
     * @param value
     */
    public void putContextProperty(String name,Object value){
        if(name==null){
            return;
        }
        this.setValue(name,value,properties);
    }

    /**
     * 设置属性值
     * @param key
     * @param value
     */
    private void setValue(String key,Object value, Map<String,Object> yamlMap){
        if(key==null){
            return;
        }
        String[] keys = key.split("[.]");
        Object proValue= yamlMap.get(keys[0]);
        if(keys.length>1&&proValue instanceof Map){
            setValue(key.substring(key.indexOf(".")+1),value,( Map<String, Object>)proValue);
        }else{
            yamlMap.put(key,value);
        }
    }

    /**
     * 得到上下文属性
     * @param name
     * @return
     */
    public Object getContextProperty(String name){
        Object value=null;
        value=this.getValue(name);
        if(value==null){
            return "";
        }
        return value;
    }
    /**
     * 得到属性值
     * @param key
     * @return
     */
    private Object getValue(String key){
        return this.getValue(key,properties);
    }
    /**
     * 得到属性值
     * @param key
     * @param yamlMap
     * @return
     */
    private Object getValue(String key,Map<String,Object> yamlMap){
        Object value=null;
        if(key==null){
            return null;
        }
        String[] keys = key.split("[.]");
        value = yamlMap.get(keys[0]);
        if(keys.length>1&&value instanceof Map){
            return getValue(key.substring(key.indexOf(".")+1),( Map<String, Object>)value);
        }
        return value;
    }
    /**
     * 清除属性内存
     */
    public void clear(){
        properties.clear();
    }
}
