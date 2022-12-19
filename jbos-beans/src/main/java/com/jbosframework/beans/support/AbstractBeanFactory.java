package com.jbosframework.beans.support;
import com.jbosframework.beans.BeansException;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.factory.BeanTypeException;
import com.jbosframework.utils.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractBeanFactory
 * @author youfu.wang
 * @version 1.0
 */
public abstract class AbstractBeanFactory implements ConfigurableBeanFactory {
    private static final Log logger= LogFactory.getLog(AbstractBeanFactory.class);
    protected final static Map<String,Object> singletonInstances= new ConcurrentHashMap(256);
    private volatile List<BeanPostProcessor> beanPostProcessors=new ArrayList(256);

    public Object getBean(String name) {
        return this.getBean(name,null);
    }

    public <T> T getBean(String name,Class<T> requiredType){
        Assert.notEmpty(name, "bean must not be empty");
        return this.doGetBean(name,requiredType);
    }

    public <T> T getBean(Class<T> requiredType){
        if(requiredType==null){
            return null;
        }
        return this.doGetBean(requiredType.getName(),requiredType);
    }

    private <T> T doGetBean(String name,Class<T> requiredType){
        BeanDefinition beanDefinition=this.getBeanDefinition(name);
        Object obj=this.createBean((GenericBeanDefinition)beanDefinition);
        T bean=this.convertBean(obj,requiredType);
        if(beanDefinition.isSingleton()){
            this.registerSingletonInstance(name,bean);
        }
        return bean;
    }
    private <T> T convertBean(Object obj,Class<T> requiredType){
        if(obj!=null&&requiredType!=null){
            if((obj.getClass().getName().equals(requiredType.getName())||requiredType.isAssignableFrom(obj.getClass()))
                    ||obj.getClass().getName().contains("com.sun.proxy.$Proxy")
                    ||obj.getClass().getName().contains("$$EnhancerByCGLIB$$")){
                return (T)obj;
            }else{
                BeanTypeException ex =new BeanTypeException("给定的Bean名称"+requiredType.getName()+"与"+obj.getClass().getName()+"不相同");
                ex.printStackTrace();
                return null;
            }
        }
        return (T)obj;
    }

    public boolean isSingleton(String name) {
        if(this.containsBean(name)){
            BeanDefinition beanDefinition=this.getBeanDefinition(name);
            if(beanDefinition.isSingleton())
                return true;
        }
        return false;
    }

    public boolean isPrototype(String name) {
        if(this.containsBean(name)){
            BeanDefinition beanDefinition=this.getBeanDefinition(name);
            if(beanDefinition.isPrototype())
                return true;
        }
        return false;
    }


    public Object getSingletonInstance(String name){
        return this.singletonInstances.get(name);
    }

    public void registerSingletonInstance(String name,Object object){
        synchronized (this.singletonInstances){
            this.singletonInstances.put(name,object);
            Class<?>[] interfaces=object.getClass().getInterfaces();
            for(Class<?> interfaceCls:interfaces){
                this.singletonInstances.put(interfaceCls.getName(),object);
            }
            Class<?> superclass=object.getClass().getSuperclass();
            if(superclass!=null){
                this.singletonInstances.put(superclass.getName(),object);
            }
        }
    }

    public boolean containsSingletonBean(String name){
        return this.singletonInstances.containsKey(name);
    }

    public void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        if(!beanPostProcessors.contains(beanPostProcessor)){
            beanPostProcessors.add(beanPostProcessor);
        }
    }
    public List<BeanPostProcessor> getBeanPostProcessors(){
        return beanPostProcessors;
    }
    public abstract Object createBean(GenericBeanDefinition genericBeanDefinition) throws BeansException;
    public abstract boolean containsBean(String name);

    public abstract BeanDefinition getBeanDefinition(String name) throws BeansException;
    protected void doPostProcessBeforeInitialization(Object bean,GenericBeanDefinition genericBeanDefinition){
        for(BeanPostProcessor beanPostProcessor:beanPostProcessors){
            beanPostProcessor.postProcessBeforeInitialization(bean,genericBeanDefinition);
        }
    }
    protected void doPostProcessAfterInitialization(Object bean,GenericBeanDefinition genericBeanDefinition){
        for(BeanPostProcessor beanPostProcessor:beanPostProcessors){
            beanPostProcessor.postProcessAfterInitialization(bean,genericBeanDefinition);
        }
    }
}
