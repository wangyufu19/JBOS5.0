package com.jbosframework.context.configuration;

import com.jbosframework.core.io.ClassPathResource;
import com.jbosframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * YamlConfig
 * @author youfu.wang
 * @date 2020-10-10
 */

public class YamlConfig {
    private String configLocation;
    private static Map<String, Object> properties=Collections.synchronizedMap(new LinkedHashMap<String,Object>());
    private Environment environment;
    /**
     * 构造方法
     */
    public YamlConfig(){
        configLocation="jbosContext";
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
     * 构造方法
     * @param configLocation
     */
    public YamlConfig(String configLocation){
        this.configLocation=configLocation;
    }
    /**
     * 加载配置属性
     */
    public void load(Resource resource){
        //加载主配置
        properties=this.getConfigProperties(resource);
        String activeProfile="";
        if(this.getValue(Configuration.CTX_PROPERTY_PROFILES_ACTIVE)!=null){
            activeProfile=String.valueOf(this.getValue(Configuration.CTX_PROPERTY_PROFILES_ACTIVE));
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
     * 设置属性值
     * @param key
     * @param value
     */
    public void setValue(String key,Object value){
        properties.put(key,value);
    }
    /**
     * 得到属性值
     * @param key
     * @return
     */
    public Object getValue(String key){
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
