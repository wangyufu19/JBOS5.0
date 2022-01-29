package com.jbosframework.beans.support;
import com.jbosframework.beans.BeanException;
import com.jbosframework.beans.config.BeanBeforeProcessor;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.beans.factory.BeanTypeException;
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
    protected final Map<String,Object> singletonInstances= new ConcurrentHashMap(256);
    private volatile List<BeanBeforeProcessor> beanBeforeProcessors= new ArrayList(256);
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
        return this.convertBean(obj,requiredType);
    }
    private <T> T convertBean(Object obj,Class<T> requiredType){
        if(obj!=null&&requiredType!=null){
            if(obj.getClass().getName().equals(requiredType.getName())||obj.getClass().getName().contains("com.sun.proxy.$Proxy")||obj.getClass().getName().contains("$$EnhancerByCGLIB$$")){
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

    /**
     * 是否包含该Bean对象
     * @param name
     * @return
     */
    public boolean containsSingletonBean(String name){
        return this.singletonInstances.containsKey(name);
    }
    /**
     * 处理Bean对象的Processor
     * @param bean
     * @param beanDefinition
     * @return
     */
    public void doBeanBeforeProcessor(Object bean, BeanDefinition beanDefinition){
        for(BeanBeforeProcessor beanBeforeProcessor:this.getBeanBeforeProcessors()){
            beanBeforeProcessor.process(bean,beanDefinition);
        }
    }
    /**
     * 处理Bean对象的Processor
     */
    public Object doBeanPostProcessor(Object bean){
        Object obj=bean;
        if(obj==null){
            return null;
        }
        for(BeanPostProcessor beanPostProcessor:this.getBeanPostProcessors()){
            obj=beanPostProcessor.process(obj);
        }
        return obj;
    }
    /**
     * 添加BeanBeforeProcessor
     * @param beanBeforeProcessor
     */
    public void addBeanBeforeProcessor(BeanBeforeProcessor beanBeforeProcessor){
        beanBeforeProcessors.add(beanBeforeProcessor);
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
    public List<BeanBeforeProcessor> getBeanBeforeProcessors(){
        return beanBeforeProcessors;
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
