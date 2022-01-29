package com.jbosframework.context.support;
import com.jbosframework.beans.factory.DefaultBeanFactory;
import com.jbosframework.context.ApplicationContextFactory;
import com.jbosframework.context.ApplicationEventListener;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.context.configuration.Environment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public abstract class AbstractApplicationContext extends DefaultBeanFactory implements ConfigurableApplicationContext {
    private static final Log log= LogFactory.getLog(AbstractApplicationContext.class);
    private volatile List<PropertyPostProcessor> propertyPostProcessors= new ArrayList(256);
    private Set<EventListener> listeners;
    private Environment environment=new Environment();
    private static Map<String, Object> properties= new ConcurrentHashMap(256);

    public AbstractApplicationContext(){
        this.listeners=new LinkedHashSet<EventListener>();
    }

    public void refresh(){

        this.finishRefresh();
    }

    private void addApplicationEventListener(ApplicationEventListener listener){

    }

    private void finishRefresh(){
        ApplicationContextFactory.setApplicationContext(this);
    }

    public void addPropertyPostProcessor(PropertyPostProcessor propertyPostProcessor){
        propertyPostProcessor.loadProperty(environment,properties);
        propertyPostProcessors.add(propertyPostProcessor);
        Collections.sort(propertyPostProcessors, new Comparator<PropertyPostProcessor>() {
            @Override
            public int compare(PropertyPostProcessor o1, PropertyPostProcessor o2) {
                return o1.getOrder()-o2.getOrder();
            }
        });
    }
    public String getPropertyValue(String name){
        Object value="";
        for(PropertyPostProcessor propertyPostProcessor:this.propertyPostProcessors){
            value=propertyPostProcessor.getPropertyValue(properties,name);
            if(value!=null){
                break;
            }
        }
        return value.toString();
    }
}