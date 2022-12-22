package com.jbosframework.context.annotation.aspectj;

import com.jbosframework.aop.aspectj.support.AspectMetadata;
import com.jbosframework.context.ConfigurableApplicationContext;
import org.eclipse.jetty.util.ConcurrentHashSet;
import java.util.Set;


public class AspectjApplicationContext {
    protected final Set<AspectMetadata> aspectMetadataMap=new ConcurrentHashSet();

    private ConfigurableApplicationContext applicationContext;

    public AspectjApplicationContext(ConfigurableApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }

    public void putAspectMetadata(AspectMetadata aspectMetadata){
        aspectMetadataMap.add(aspectMetadata);
    }
    public AspectMetadata getMetadata(String pointcut){
        return null;
    }
    public boolean contains(String pointcut){
        return false;
    }
}
