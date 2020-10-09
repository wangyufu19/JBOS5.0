package com.jbosframework.context.configuration;

import com.jbosframework.core.io.ClassPathResource;
import com.jbosframework.core.io.Resource;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class YamlConfig {
    private String configLocation;
    private static Map<String, Object> properties=Collections.synchronizedMap(new LinkedHashMap<String,Object>());

    /**
     * 构造方法
     */
    public YamlConfig(){
        configLocation="jbosContext.yml";
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
    public void load(){
        this.load(configLocation);
    }

    /**
     * 加载配置属性
     * @param configLocation
     */
    public void load(String configLocation){
        try {
            Resource resource=new ClassPathResource(configLocation);
            Yaml yaml = new Yaml();
            properties=yaml.loadAs(resource.getInputStream(), LinkedHashMap.class);
            log.info("*****properties: "+properties.get("mybatis"));
        } catch (IOException e) {
            e.printStackTrace();
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
