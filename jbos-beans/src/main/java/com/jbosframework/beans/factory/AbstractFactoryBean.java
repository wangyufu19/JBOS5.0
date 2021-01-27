package com.jbosframework.beans.factory;

import com.jbosframework.beans.annotation.Value;
import com.jbosframework.beans.config.*;
import com.jbosframework.core.jepl.JEPL;
import com.jbosframework.utils.JBOSClassCaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * AbstractFactoryBean
 * @author youfu.wang
 * @version 5.0
 */
public abstract class AbstractFactoryBean implements BeanFactory{
    private static final Log log= LogFactory.getLog(AbstractFactoryBean.class);

    private static List<BeanBeforeProcessor> beanBeforeProcessors= Collections.synchronizedList(new ArrayList<BeanBeforeProcessor>());

    private static List<BeanPostProcessor> beanPostProcessors=Collections.synchronizedList(new LinkedList<BeanPostProcessor>());

    public abstract Map<String,Object> getSingletonInstances();

    /**
     * 初始化Bean
     * @param beanDefinition
     * @return
     */
    public Object initBean(BeanDefinition beanDefinition){
        Object obj=null;
        if(beanDefinition==null){
            return null;
        }
        //Bean实例化
        if(this.getSingletonInstances().containsKey(beanDefinition.getName())){
            obj=getSingletonInstances().get(beanDefinition.getName());
        }else{
            if (beanDefinition.isMethodBean()){
                obj=this.doCreateMethodBean(beanDefinition);
            }else{
                obj=BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName());
            }
        }
        //执行初始化方法
        //执行BeanBeforeProcessor
        this.doBeanBeforeProcessor(obj,beanDefinition);
        if(beanDefinition.isSingleton()){
            this.putBean(beanDefinition.getName(),obj);
        }
        return obj;
    }
    /**
     * 创建方法Bean
     * @param beanDefinition
     * @return
     */
    private Object doCreateMethodBean(BeanDefinition beanDefinition){
        Object obj=null;
        Object parentObj=this.getBean(beanDefinition.getParentName());
        MethodMetadata methodMetadata=beanDefinition.getMethodMetadata();
        if(methodMetadata.getMethodParameters().length>0){
            Object[] parameterValues=new Object[methodMetadata.getMethodParameters().length];
            for(int i=0;i<methodMetadata.getMethodParameters().length;i++){
                parameterValues[i]=this.getBean(methodMetadata.getMethodParameters()[i].getType().getName());
            }
            obj= JBOSClassCaller.call(parentObj,methodMetadata.getMethodName(),parameterValues,methodMetadata.getParameterTypes());
        }else{
            obj=JBOSClassCaller.call(parentObj,methodMetadata.getMethodName());
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
        beanPostProcessors.add(beanPostProcessor);
        Collections.sort(this.getBeanPostProcessors(), new Comparator<BeanPostProcessor>() {
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
     * 处理Bean对象的Processor
     * @param bean
     * @param beanDefinition
     * @return
     */
    private void doBeanBeforeProcessor(Object bean,BeanDefinition beanDefinition){
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

    public void clear(){
        beanBeforeProcessors.clear();
        beanPostProcessors.clear();
    }
}
