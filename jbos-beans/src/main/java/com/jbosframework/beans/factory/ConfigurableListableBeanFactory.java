package com.jbosframework.beans.factory;

import com.jbosframework.beans.BeansException;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.support.AbstractBeanFactory;
import com.jbosframework.beans.support.BeanDefinitionRegistry;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.JBOSClassCaller;
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
public class ConfigurableListableBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegistry {
    private static final Log logger= LogFactory.getLog(ConfigurableListableBeanFactory.class);

    //XML and Annotation IoC Bean
    protected final Map<String,BeanDefinition> beanDefinitions=new ConcurrentHashMap(256);
    //Bean Name Map
    protected final Map<String, List<BeanDefinition>> allBeanNamesByType=new ConcurrentHashMap(64);

    private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap(64);

    private volatile List<String> beanDefinitionNames = new ArrayList(256);


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
        return new String[0];
    }

    public <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
        Assert.notNull(requiredType, " RequiredType must not be null");
        Map<String, T> beansTypesMap=new HashMap<String, T>();
        return beansTypesMap;
    }

    public void putBeanDefinition(String name,BeanDefinition beanDefinition) {
        if(logger.isDebugEnabled()){
            logger.info("register bean ["+beanDefinition.getClassName()+"]");
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
        Class<?>[] interfaces=beanDefinition.getBeanClass().getInterfaces();
        for(Class<?> interfaceCls:interfaces){
            this.putBeanNameOfType(interfaceCls.getName(),beanDefinition);
        }
    }
    public void initialization(){
        for(Map.Entry<String,BeanDefinition> entry:this.beanDefinitions.entrySet()){
            if(entry.getValue().getRole()!=BeanDefinition.ROLE_APPLICATION&&entry.getValue().getRole()!=BeanDefinition.ROLE_MEMBER_METHOD){
                Object bean=BeanInstanceUtils.newBeanInstance(((GenericBeanDefinition)entry.getValue()).getBeanClass());
                this.registerSingletonInstance(entry.getKey(),bean);
            }
        }
    }
    public Object createBean(GenericBeanDefinition genericBeanDefinition) throws BeansException{
        Object bean;
        if(this.containsSingletonBean(genericBeanDefinition.getName())){
            bean=this.getSingletonInstance(genericBeanDefinition.getName());
        }else{
            if(genericBeanDefinition.getRole()==BeanDefinition.ROLE_MEMBER_METHOD){
                bean=this.invokeMethodBean(genericBeanDefinition);
            }else{
                bean=BeanInstanceUtils.newBeanInstance(genericBeanDefinition.getBeanClass());
            }
        }
        this.doPostProcessBeforeInitialization(bean,genericBeanDefinition);
        this.dopostProcessAfterInitialization(bean,genericBeanDefinition);
        if(genericBeanDefinition.isSingleton()){
            this.registerSingletonInstance(genericBeanDefinition.getName(),bean);
        }
        return bean;
    }
    private Object invokeMethodBean(GenericBeanDefinition genericBeanDefinition){
        Object parentBean=this.createBean((GenericBeanDefinition)genericBeanDefinition.getParent());
        MethodMetadata methodMetadata=genericBeanDefinition.getMethodMetadata();
        if(methodMetadata.getMethodParameters().length>0){
            Object[] parameterValues=new Object[methodMetadata.getMethodParameters().length];
            for(int i=0;i<methodMetadata.getMethodParameters().length;i++){
                Object refObj=this.getBean(methodMetadata.getMethodParameters()[i].getType().getName());
                parameterValues[i]=refObj;
            }
            return JBOSClassCaller.call(parentBean,methodMetadata.getMethod(),parameterValues,methodMetadata.getParameterTypes());
        }else{
            return JBOSClassCaller.call(parentBean,methodMetadata.getMethod());
        }
    }
    public BeanDefinition getBeanDefinition(String name) throws BeansException {
        BeanDefinition beanDefinition=this.beanDefinitions.get(name);
        Assert.notNull(beanDefinition, name + " BeanDefinition is not exists");
        return beanDefinition;
    }

    public boolean containsBean(String name){
        return this.beanDefinitions.containsKey(name);
    }

    public List<String> getBeanDefinitionNames(){
        return this.beanDefinitionNames;
    }

    public void destroy() {
        singletonInstances.clear();
        beanDefinitions.clear();
        beanDefinitions.clear();
        allBeanNamesByType.clear();
        singletonBeanNamesByType.clear();
    }

}
