package com.jbosframework.beans.support;
import com.jbosframework.beans.BeanException;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.beans.factory.*;
import com.jbosframework.core.Nullable;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;



/**
 * AbstractAutowireBeanFactory
 * @author youfu.wang
 * @version 1.0
 */
public abstract class AbstractAutowireBeanFactory extends AbstractBeanFactory implements AutowireBeanFactory, ConfigurableBeanFactory {
    private static final Log log= LogFactory.getLog(AbstractAutowireBeanFactory.class);


    public Object initializeBean(Object existingBean, String beanName) throws BeanException{
        return null;
    }

    public void autowireBean(Object existingBean) throws BeanException{
        Assert.notNull(existingBean, "Object must not be null");
        Field[] fields=existingBean.getClass().getDeclaredFields();
        if(fields==null) {
            return;
        }
        InjectionMetadata injectionMetadata=new InjectionMetadata();
        for(int i=0;i<fields.length;i++) {
            Autowired autowiredAnnotation=fields[i].getDeclaredAnnotation(Autowired.class);
            if(autowiredAnnotation==null) {
                continue;
            }
            //校验字段注解是否用在了static方法上
            if (Modifier.isStatic(fields[i].getModifiers())) {
                if (log.isWarnEnabled()) {
                    log.warn("Field com.jbosframework.beans.annotation is not supported on static fields: " + fields[i].getName());
                }
                return;
            }
            Object fieldValue=null;
            if(fields[i].getType().isInterface()){
                String[] beanNames=this.getBeanNamesOfType(fields[i].getType());
                if(beanNames.length<=0){
                    fieldValue=this.getBean(fields[i].getType().getName());
                }else{
                    if(beanNames.length>1){
                        BeanTypeException ex = new BeanTypeException("指定的类型Bean'" + fields[i].getType() + "' available:找到多个实现Bean["+ StringUtils.stringArrayToTokenize(beanNames,",") +"]");
                        ex.printStackTrace();
                        return;
                    }else{
                        fieldValue=this.getBean(beanNames[0]);
                    }
                }
            }else{
                fieldValue=this.getBean(fields[i].getType().getName());
            }
            injectionMetadata.inject(existingBean,fields[i],fieldValue);
        }
    }

    public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeanException{
        return null;
    }
    /**
     * 创建Bean对象
     * @param name
     * @param beanDefinition
     * @param args
     * @return
     * @throws BeanException
     */
    public Object createBean(String name, BeanDefinition beanDefinition, @Nullable Object[] args) throws BeanException{
        Object obj=null;
        //Bean实例化
        if(this.containsSingletonBean(name)){
            obj=this.getSingletonInstance(name);
        }else{
            if(beanDefinition.isMethodBean()){

            }else{
                obj=BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName());
            }
        }
        this.autowireBean(obj);
        return obj;
    }
    public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeanException{
        return null;
    }

    public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws BeanException{

    }
}
