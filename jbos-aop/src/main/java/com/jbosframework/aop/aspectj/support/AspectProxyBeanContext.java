package com.jbosframework.aop.aspectj.support;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * AspectProxyBeanContext
 * @author youfu.wang
 * @version 1.0
 */
public class AspectProxyBeanContext {
    private static Log log= LogFactory.getLog(AspectProxyBeanContext.class);
    //Aspect Metadata Bean
    protected static Map<String, AspectMetadata> metadataMap= Collections.synchronizedMap(new LinkedHashMap<String, AspectMetadata>());

    public boolean contains(String pointcut){
        if(pointcut==null){
            return false;
        }
        if(metadataMap.containsKey(pointcut)){
            return true;
        }
        return false;
    }
    public void putMetadata(AspectMetadata metadata){
        if(!metadataMap.containsKey(metadata.getPointcut())){
            metadataMap.put(metadata.getPointcut(),metadata);
        }
    }
    public AspectMetadata getMetadata(String pointcut){
        if(metadataMap.containsKey(pointcut)){
            return metadataMap.get(pointcut);
        }
        return null;
    }
    public Map<String, AspectMetadata> getMetadataMap(){
        return metadataMap;
    }
}
