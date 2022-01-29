package com.jbosframework.boot.context;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextInitializer;
import com.jbosframework.context.configuration.Environment;
import com.jbosframework.context.support.PropertyPostProcessor;
import com.jbosframework.core.Order;
import com.jbosframework.core.io.ClassPathResource;
import com.jbosframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * ConfigFileApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public class ConfigFileApplicationContext implements ApplicationContextInitializer {


    public void initialize(ApplicationContext applicationContext) {
        applicationContext.addPropertyPostProcessor(new ConfigPropertyProcessor());
    }

    public class ConfigPropertyProcessor implements PropertyPostProcessor {
        private String configLocation;
        public static final String CTX_PROPERTY_PROFILES_ACTIVE="jbos.profiles.active";
        public static final String CTX_PROPERTY_CLASSPATH="jbos.classpath";
        private int order= Order.MIN;
        private Yaml yaml = new Yaml();

        public ConfigPropertyProcessor(){
            this.configLocation="jbosContext";
        }
        public void setOrder(int order){
            this.order=order;
        }
        public int getOrder() {
            return this.order;
        }
        public int compareTo(PropertyPostProcessor propertyPostProcessor) {
            return this.order - propertyPostProcessor.getOrder();
        }

        /**
         * 加载属性
         * @param environment
         * @param properties
         */
        public void loadProperty(Environment environment,Map<String, Object> properties) {
            Resource resource=new ClassPathResource(configLocation+".yml");
            //加载主配置
            this.loadConfigProperties(resource,properties);
            String activeProfile="";
            if(this.getPropertyValue(ConfigPropertyProcessor.CTX_PROPERTY_PROFILES_ACTIVE,properties)!=null){
                activeProfile=String.valueOf(this.getPropertyValue(ConfigPropertyProcessor.CTX_PROPERTY_PROFILES_ACTIVE,properties));
            }
            environment.setActiveProfile(activeProfile);
            //加载Profile配置
            this.loadProfileConfig(activeProfile,properties);
            try {
                String configClassPath=resource.getFile().getParentFile().getAbsolutePath();
                this.putPropertyValue(ConfigPropertyProcessor.CTX_PROPERTY_CLASSPATH,configClassPath,properties);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * 加载配置属性
         * @param resource
         */
        private void loadConfigProperties(Resource resource,Map<String, Object> properties){
            try {
                Map<String, Object> configPros=yaml.loadAs(resource.getInputStream(), LinkedHashMap.class);
                if(configPros!=null){
                    for(Map.Entry<String,Object> entry:configPros.entrySet()){
                        if(properties.containsKey(entry.getKey())){
                            Map<String, Object> mainConfigPros=(Map<String, Object>)properties.get(entry.getKey());
                            Map<String, Object> subConfigPros=(Map<String, Object>)entry.getValue();
                            for(Map.Entry<String,Object> sub:subConfigPros.entrySet()){
                                mainConfigPros.put(sub.getKey(),sub.getValue());
                                properties.put(entry.getKey(),mainConfigPros);
                            }
                        }else{
                            properties.put(entry.getKey(),entry.getValue());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * 加载Profile配置
         * @param activeProfile
         */
        private void loadProfileConfig(String activeProfile,Map<String, Object> properties){
            Resource resource=new ClassPathResource(configLocation+"-"+activeProfile+".yml");
            this.loadConfigProperties(resource,properties);
        }
        /**
         * 设置属性值
         * @param key
         * @param value
         */
        private void putPropertyValue(String key,Object value, Map<String,Object> yamlMap){
            if(key==null){
                return;
            }
            String[] keys = key.split("[.]");
            Object proValue= yamlMap.get(keys[0]);
            if(keys.length>1&&proValue instanceof Map){
                putPropertyValue(key.substring(key.indexOf(".")+1),value,( Map<String, Object>)proValue);
            }else{
                yamlMap.put(key,value);
            }
        }
        /**
         * 得到属性值
         * @param key
         * @return
         */
        public Object getPropertyValue(Map<String, Object> properties,String key){
            return this.getPropertyValue(key,properties);
        }
        /**
         * 得到属性值
         * @param key
         * @param yamlMap
         * @return
         */
        private Object getPropertyValue(String key,Map<String,Object> yamlMap){
            Object value=null;
            if(key==null){
                return null;
            }
            String[] keys = key.split("[.]");
            value = yamlMap.get(keys[0]);
            if(keys.length>1&&value instanceof Map){
                return getPropertyValue(key.substring(key.indexOf(".")+1),( Map<String, Object>)value);
            }
            return value;
        }
    }
}
