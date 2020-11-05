package com.jbosframework.beans.factory;
import com.jbosframework.beans.config.BeanBeforeProcessor;
import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.support.BeanFactoryContext;
import com.jbosframework.core.JBOSClassCaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AbstractBeanObjectFactory
 * @author youfu.wang
 * @version 1.0
 */
public class AbstractBeanObjectFactory implements BeanObjectFactory {
    private static final Log log= LogFactory.getLog(AbstractBeanObjectFactory.class);
    private BeanFactoryContext ctx;
    private Object bean;
    /**
     * 构造方法
     * @param ctx
     */
    public AbstractBeanObjectFactory(BeanFactoryContext ctx){
        this.ctx=ctx;
    }

    /**
     * 创建Bean对象
     * @param beanDefinition
     * @return
     */
    public Object doCreateBean(BeanDefinition beanDefinition){
        if(beanDefinition==null){
            return null;
        }
        if(beanDefinition.isSingleton()){
            if(ctx.getSingletonInstances().containsKey(beanDefinition.getName())){
                bean=ctx.getSingletonInstances().get(beanDefinition.getName());
            }else{
                bean=BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName());
            }
        }else if(beanDefinition.isPrototype()){
            if (beanDefinition.isMethodBean()){
                bean=this.doCreateMethodBean(beanDefinition);
            }else{
                bean=BeanInstanceUtils.newBeanInstance(beanDefinition.getClassName());
            }
        }
        if(bean==null){
            BeanTypeException ex = new BeanTypeException("Qualifying bean of type '" + beanDefinition.getName() + "' available");
            ex.printStackTrace();
        }
        bean=this.doBeanBeforeProcessor(bean,beanDefinition);
        bean=this.doBeanPostProcessor(bean,beanDefinition);
        if(beanDefinition.isSingleton()){
            ctx.putBean(beanDefinition.getName(),bean);
        }
        return bean;
    }

    /**
     * 处理Bean对象的Processor
     * @param bean
     * @param beanDefinition
     * @return
     */
    private Object doBeanBeforeProcessor(Object bean,BeanDefinition beanDefinition){
        Object obj=bean;
        Object tmp=null;
        for(BeanBeforeProcessor beanBeforeProcessor:ctx.getBeanBeforeProcessors()){
            tmp=beanBeforeProcessor.process(bean);
            if(tmp!=null){
                obj=tmp;
            }
        }
        return obj;
    }
    /**
     * 处理Bean对象的Processor
     */
    private Object doBeanPostProcessor(Object bean,BeanDefinition beanDefinition){
        Object obj=bean;
        Object tmp=null;
        for(BeanPostProcessor beanPostProcessor:ctx.getBeanPostProcessors()){
            tmp=beanPostProcessor.process(bean,beanDefinition);
            if(tmp!=null){
                obj=tmp;
            }
        }
        return obj;
    }
    /**
     * 创建方法Bean对象
     * @param beanDefinition
     */
    private Object doCreateMethodBean(BeanDefinition beanDefinition){
        Object obj;
        BeanDefinition parentBeanDefinition=ctx.getBeanDefinition(beanDefinition.getParentName());
        Object parentObj=ctx.getBean(parentBeanDefinition.getName());
        obj= JBOSClassCaller.call(parentObj,beanDefinition.getClassMethod());
        return obj;
    }

}
