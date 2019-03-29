package com.jbosframework.aspectj.support;
import com.jbosframework.aspectj.metadata.Metadata;
import com.jbosframework.context.annotation.EnableAspectJAutoProxy;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * AspectProxyBeanContext
 * @author youfu.wang
 * @version 1.0
 */
public class AspectProxyBeanContext {
    //Aspect Metadata Bean
    protected static Map<String,Metadata> metadatas= Collections.synchronizedMap(new LinkedHashMap<String,Metadata>());

    public boolean contains(String pointcut){
        if(pointcut==null){
            return false;
        }
        if(metadatas.containsKey(pointcut)){
            return true;
        }
        return false;
    }
    public void putMetadata(Metadata metadata){
        if(!metadatas.containsKey(metadata.getPointcut())){
            metadatas.put(metadata.getPointcut(),metadata);
        }
    }
    public Metadata getMetadata(String pointcut){
        if(metadatas.containsKey(pointcut)){
            return metadatas.get(pointcut);
        }
        return null;
    }
}
