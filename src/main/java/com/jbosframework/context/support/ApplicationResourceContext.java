package com.jbosframework.context.support;
import com.jbosframework.beans.config.BeanProperty;
import com.jbosframework.context.Environment;
import com.jbosframework.core.io.ClassPathResource;
import com.jbosframework.core.io.Resource;
import com.jbosframework.core.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.util.*;

/**
 * ApplicationResourceContext
 * @author youfu.wang
 * @version 1.0
 */
public class ApplicationResourceContext {
    private static Log log= LogFactory.getLog(ApplicationResourceContext.class);
    private static String defaultConfigLocation;
    protected static Map<String,String> contextProperties= Collections.synchronizedMap(new LinkedHashMap<String,String>());

    static{
        defaultConfigLocation="application.properties";
    }

    /**
     * 构造方法
     */
    public ApplicationResourceContext(){

    }

    /**
     * 初始化资源
     */
    public void init(){
        Resource resource=new ClassPathResource(defaultConfigLocation);
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
     * 注入上下文属性
     * @param properties
     */
    public void putContextProperties(List<BeanProperty> properties){
        if(properties==null) {
            return;
        }
        for(int i=0;i<properties.size();i++){
            BeanProperty pro=properties.get(i);
            contextProperties.put(pro.getName(), pro.getValue());
        }
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

}
