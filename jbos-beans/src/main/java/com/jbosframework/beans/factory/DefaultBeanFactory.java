package com.jbosframework.beans.factory;

import com.jbosframework.beans.BeanException;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.support.AbstractAutowireBeanFactory;
import com.jbosframework.utils.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultBeanFactory
 * @author youfu.wang
 * @version 1.0
 */
public class DefaultBeanFactory extends AbstractAutowireBeanFactory {
    private static final Log log= LogFactory.getLog(DefaultBeanFactory.class);

    //XML and Annotation IoC Bean
    protected final Map<String,BeanDefinition> beanDefinitions=new ConcurrentHashMap(256);
    //Bean Name Map
    protected final Map<String, List<BeanDefinition>> allBeanNamesByType=new ConcurrentHashMap(64);

    private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap(64);

    private volatile List<String> beanDefinitionNames = new ArrayList(256);
    /**
     * 销毁Bean对象内存
     */
    public void destroy() {
        singletonInstances.clear();
        beanDefinitions.clear();
        beanDefinitions.clear();
        allBeanNamesByType.clear();
        singletonBeanNamesByType.clear();
    }
    /**
     * 注册Bean定义对象
     * @param name
     * @param beanDefinition
     */
    public void putBeanNameOfType(String name, BeanDefinition beanDefinition) {
        Assert.notNull(beanDefinition, " BeanDefinition must not be null");
        List<BeanDefinition> beanDefinitions=null;
        synchronized(this.allBeanNamesByType) {
            if(allBeanNamesByType.containsKey(name)){
                beanDefinitions=(List<BeanDefinition>)allBeanNamesByType.get(name);
            }else{
                beanDefinitions=new ArrayList<BeanDefinition>();
            }
            if(beanDefinitions!=null){
                beanDefinitions.add(beanDefinition);
                allBeanNamesByType.remove(name);
                allBeanNamesByType.put(name,beanDefinitions);
            }
        }
    }

    /**
     * 根据类名称得到Bean定义对象
     * @param requiredType
     * @return
     */
    public <T> String[] getBeanNamesOfType(Class<T> requiredType) {
        Assert.notNull(requiredType, " RequiredType must not be null");
        List<BeanDefinition> beanDefinitions=this.allBeanNamesByType.get(requiredType.getName());
        if(beanDefinitions!=null){
            List<String> beanNames=new ArrayList<String>(beanDefinitionNames.size());
            for(BeanDefinition beanDefinition:beanDefinitions){
                beanNames.add(beanDefinition.getClassName());
            }
            return beanNames.toArray(new String[beanNames.size()]);
        }
        return null;
    }

    /**
     * 根据类名称得到Bean定义对象
     * @param requiredType
     * @return
     */
    public <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
        Assert.notNull(requiredType, " RequiredType must not be null");
        Map<String, T> beansTypesMap=new HashMap<String, T>();
        return beansTypesMap;
    }
    /**
     * 注入Bean定义对象
     * @param beanDefinition
     */
    public void putBeanDefinition(String name,BeanDefinition beanDefinition) {
        if(log.isDebugEnabled()){
            log.info("register bean ["+beanDefinition.getClassName()+"]");
        }
        Assert.notEmpty(name, beanDefinition.getClassName()+ " bean must not be empty");
        Assert.notNull(beanDefinition, " BeanDefinition must not be null");
        BeanDefinition existingDefinition = (BeanDefinition)this.beanDefinitions.get(name);
        if(existingDefinition!=null){
            this.beanDefinitions.put(name, beanDefinition);
        }else{
            synchronized(this.beanDefinitions) {
                this.beanDefinitions.put(name, beanDefinition);
                List<String> updatedDefinitions = new ArrayList(this.beanDefinitionNames.size() + 1);
                updatedDefinitions.addAll(this.beanDefinitionNames);
                updatedDefinitions.add(name);
                this.beanDefinitionNames = updatedDefinitions;
            }
        }
        this.putBeanNameOfType(beanDefinition.getClassName(),beanDefinition);
    }

    /**
     * 得到Bean定义
     * @param name
     * @return
     */
    public BeanDefinition getBeanDefinition(String name) throws BeanException{
        BeanDefinition beanDefinition=this.beanDefinitions.get(name);
        Assert.notNull(beanDefinition, name + " BeanDefinition is not exists");
        return beanDefinition;
    }

    /**
     * 是否包含该Bean
     * @param name
     * @return
     */
    public boolean containsBean(String name){
        return this.beanDefinitions.containsKey(name);
    }

}
