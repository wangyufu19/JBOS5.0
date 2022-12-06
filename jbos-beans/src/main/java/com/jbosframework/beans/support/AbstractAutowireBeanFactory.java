package com.jbosframework.beans.support;
import com.jbosframework.beans.BeansException;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.AbstractBeanDefinition;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.beans.factory.*;
import com.jbosframework.core.Nullable;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.JBOSClassCaller;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


/**
 * AbstractAutowireBeanFactory
 * @author youfu.wang
 * @version 1.0
 */
public abstract class AbstractAutowireBeanFactory extends AbstractBeanFactory implements AutowireBeanFactory, ConfigurableBeanFactory {
    private static final Log log= LogFactory.getLog(AbstractAutowireBeanFactory.class);
    private volatile List<DependencyFactory> dependencyFactories=new ArrayList(256);


    public Object initializeBean(Object existingBean, String beanName) throws BeansException {
        return null;
    }

    public void autowireBean(Object existingBean) throws BeansException {
        Assert.notNull(existingBean, "Object must not be null");
        Field[] fields=existingBean.getClass().getDeclaredFields();
        if(fields==null) {
            return;
        }
        for(int i=0;i<fields.length;i++) {
            //校验字段注解是否用在了static方法上
            if (Modifier.isStatic(fields[i].getModifiers())) {
                if (log.isWarnEnabled()) {
                    log.warn("Field com.jbosframework.beans.annotation is not supported on static fields: " + fields[i].getName());
                }
                continue;
            }
            for(DependencyFactory dependencyFactory:dependencyFactories){
                dependencyFactory.autowireDependency(existingBean,fields[i]);
            }
        }
    }

    public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
        return null;
    }
    /**
     * 创建Bean对象
     * @param name
     * @param beanDefinition
     * @param args
     * @return
     * @throws BeansException
     */
    public Object createBean(String name, BeanDefinition beanDefinition, @Nullable Object[] args) throws BeansException {
        Object obj=null;
        //Bean实例化
        if(this.containsSingletonBean(name)){
            obj=this.getSingletonInstance(name);
            return obj;
        }else{
            if(beanDefinition.isMethodBean()){
                obj=this.createMethodBean(beanDefinition);
            }else{
                obj=BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName());
            }
        }
        //Bean初始化、依赖注入前的处理器
        this.doPostProcessBeforeInitialization(obj,beanDefinition);
        //调用Bean初始化方法
        this.invokeInitMethod(obj,beanDefinition);
        //依赖注入
        this.autowireBean(obj);
        //Bean初始化、依赖注入后的处理器
        this.doPostProcessAfterInitialization(obj,beanDefinition);
        return obj;
    }

    /**
     * 创建方法Bean对象
     * @param beanDefinition
     * @return
     */
    private Object createMethodBean(BeanDefinition beanDefinition){
        Object obj=null;
        Object parentObj=this.getBean(beanDefinition.getParentName());
        AbstractBeanDefinition defaultBeanDefinition=(AbstractBeanDefinition)beanDefinition;
        MethodMetadata methodMetadata=null;
        //defaultBeanDefinition.getMethodMetadata();
        if(methodMetadata.getMethodParameters().length>0){
            Object[] parameterValues=new Object[methodMetadata.getMethodParameters().length];
            for(int i=0;i<methodMetadata.getMethodParameters().length;i++){
                Object refObj=this.getBean(methodMetadata.getMethodParameters()[i].getType().getName());
                parameterValues[i]=refObj;
            }
            obj= JBOSClassCaller.call(parentObj,methodMetadata.getMethodName(),parameterValues,methodMetadata.getParameterTypes());
        }else{
            obj=JBOSClassCaller.call(parentObj,methodMetadata.getMethodName());
        }
        return obj;
    }
    /**
     * 调用Bean初始化方法
     * @param beanDefinition
     */
    private void invokeInitMethod(Object obj,BeanDefinition beanDefinition){
        String initMethod=beanDefinition.getInitMethod();
        if(initMethod!=null&&!"".equals(initMethod)){
            JBOSClassCaller.call(obj,initMethod);
        }
    }
    public void addBeanDependencyFactory(DependencyFactory dependencyFactory){
        this.dependencyFactories.add(dependencyFactory);
    }

    public class AutowireDependencyFactory implements DependencyFactory {

         public void autowireDependency(Object existingBean,Field field) {
            Autowired autowiredAnnotation=field.getDeclaredAnnotation(Autowired.class);
            if(autowiredAnnotation==null) {
                return;
            }
            Object fieldValue=null;
            if(field.getType().isInterface()){
                String[] beanNames=getBeanNamesOfType(field.getType());
                if(beanNames.length<=0){
                    fieldValue=getBean(field.getType().getName());
                }else{
                    if(beanNames.length>1){
                        BeanTypeException ex = new BeanTypeException("指定的类型Bean'" +field.getType() + "' available:找到多个实现Bean["+ StringUtils.stringArrayToTokenize(beanNames,",") +"]");
                        ex.printStackTrace();
                        return;
                    }else{
                        fieldValue=getBean(beanNames[0]);
                    }
                }
            }else{
                fieldValue=getBean(field.getType().getName());
            }
            this.inject(existingBean,field,fieldValue);
        }
    }
}
