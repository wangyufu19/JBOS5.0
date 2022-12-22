package com.jbosframework.context.annotation.aspectj;

import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.context.ConfigurableApplicationContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AspectjApplicationContext {
    protected final Map<String,AspectMetadata> aspectMetadataMap=new ConcurrentHashMap(256);

    private ConfigurableApplicationContext applicationContext;

    public AspectjApplicationContext(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public void putAspectMetadata(String pointcut,AspectMetadata aspectMetadata){
        aspectMetadataMap.put(pointcut,aspectMetadata);
    }
    public AspectMetadata getMetadata(String pointcut){
        return aspectMetadataMap.get(pointcut);
    }
    public boolean contains(String pointcut){
        if(aspectMetadataMap.containsKey(pointcut)){
            return true;
        }else{
            return false;
        }
    }
}
