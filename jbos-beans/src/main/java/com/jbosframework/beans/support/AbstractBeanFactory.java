package com.jbosframework.beans.support;
import com.jbosframework.beans.BeanException;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanTypeException;
import com.jbosframework.beans.factory.ImportFactory;
import com.jbosframework.core.Nullable;
import com.jbosframework.utils.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractBeanFactory
 * @author youfu.wang
 * @version 1.0
 */
public abstract class AbstractBeanFactory implements ConfigurableBeanFactory {
    protected final static Map<String,Object> singletonInstances= new ConcurrentHashMap(256);
    private volatile List<ImportFactory> importFactories= new ArrayList(256);
    private volatile List<BeanPostProcessor> beanPostProcessors=new ArrayList(256);

    /**
     * 根据名称得到Bean对象
     * @param name
     * @return
     */
    public Object getBean(String name) {
        return this.getBean(name,null);
    }
    /**
     * 根据名称和类得到Bean对象
     * @param requiredType
     * @return
     */
    public <T> T getBean(String name,Class<T> requiredType){
        Assert.notEmpty(name, "bean must not be empty");
        return this.doGetBean(name,requiredType);
    }
    /**
     * 根据类得到Bean对象
     * @param requiredType
     * @return
     */
    public <T> T getBean(Class<T> requiredType){
        if(requiredType==null){
            return null;
        }
        return this.doGetBean(requiredType.getName(),requiredType);
    }
    /**
     * 根据名称和类得到Bean对象
     * @param requiredType
     * @return
     */
    private <T> T doGetBean(String name,Class<T> requiredType){
        BeanDefinition beanDefinition=this.getBeanDefinition(name);
        Object obj=this.createBean(name,beanDefinition,null);
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
    /**
     * 是否Singleton类型Bean
     * @param name
     * @return
     */
    public boolean isSingleton(String name) {
        if(this.containsBean(name)){
            BeanDefinition beanDefinition=this.getBeanDefinition(name);
            if(beanDefinition.isSingleton())
                return true;
        }
        return false;
    }
    /**
     * 是否Prototype类型Bean
     * @param name
     * @return
     */
    public boolean isPrototype(String name) {
        if(this.containsBean(name)){
            BeanDefinition beanDefinition=this.getBeanDefinition(name);
            if(beanDefinition.isPrototype())
                return true;
        }
        return false;
    }

    /**
     * 根据名称得到Bean对象
     * @param name
     * @return
     */
    public Object getSingletonInstance(String name){
        return this.singletonInstances.get(name);
    }

    public void registerSingletonInstance(String name,Object object){
        synchronized (this.singletonInstances){
            this.singletonInstances.put(name,object);
        }
    }
    /**
     * 是否包含该Bean对象
     * @param name
     * @return
     */
    public boolean containsSingletonBean(String name){
        return this.singletonInstances.containsKey(name);
    }
    public void addImportFactory(ImportFactory importFactory){
        importFactories.add(importFactory);
    }
    public void doImportFactory(Object bean,BeanDefinition beanDefinition){
        for(ImportFactory importFactory:importFactories){
            importFactory.importBean(bean,beanDefinition);
        }
    }
    /**
     * 处理Bean对象的ProcessorP
     */
    public Object doPostProcessBeforeInitialization(Object bean,BeanDefinition beanDefinition){
        Object obj=bean;
        if(obj==null){
            return null;
        }
        for(BeanPostProcessor beanPostProcessor:this.getBeanPostProcessors()){
            obj=beanPostProcessor.postProcessBeforeInitialization(obj,beanDefinition);
        }
        return obj;
    }
    /**
     * 处理Bean对象的Processor
     */
    public Object doPostProcessAfterInitialization(Object bean,BeanDefinition beanDefinition){
        Object obj=bean;
        if(obj==null){
            return null;
        }
        for(BeanPostProcessor beanPostProcessor:this.getBeanPostProcessors()){
            obj=beanPostProcessor.postProcessAfterInitialization(obj,beanDefinition);
        }
        return obj;
    }
    /**
     * 添加BeanPostProcessor
     * @param beanPostProcessor
     */
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        if(beanPostProcessors.contains(beanPostProcessor)){
            return;
        }
        beanPostProcessors.add(beanPostProcessor);
        Collections.sort(beanPostProcessors, new Comparator<BeanPostProcessor>() {
            @Override
            public int compare(BeanPostProcessor o1, BeanPostProcessor o2) {
                return o1.getOrder()-o2.getOrder();
            }
        });
    }
    public List<BeanPostProcessor> getBeanPostProcessors(){
        return beanPostProcessors;
    }
    /**
     * 是否包含该Bean
     * @param name
     * @return
     */
    public abstract boolean containsBean(String name);
    /**
     * 得到Bean定义
     * @param name
     * @return
     */
    public abstract BeanDefinition getBeanDefinition(String name) throws BeanException;
    /**
     * 创建Bean对象
     * @param name
     * @param beanDefinition
     * @param args
     * @return
     * @throws BeanException
     */
    protected abstract Object createBean(String name,BeanDefinition beanDefinition, @Nullable Object[] args) throws BeanException;

}
