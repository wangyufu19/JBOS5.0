package com.jbosframework.boot.autoconfig.orm.mybatis;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.context.ApplicationContextAware;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
import com.jbosframework.orm.mybatis.SqlSessionTemplate;
import com.jbosframework.orm.mybatis.annotation.Mapper;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MapperBeanPostProcessor implements ApplicationContextAware,BeanPostProcessor,Ordered {
    private int order= Ordered.LOWEST_PRECEDENCE+40;
    private ConfigurableApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=(ConfigurableApplicationContext)applicationContext;
    }
    public int getOrder() {
        return order;
    }

    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition) {
        Field[] fields=bean.getClass().getDeclaredFields();
        if(fields==null){
            return bean;
        }
        Object fieldValue=null;
        for(Field field:fields){
            Mapper mapper=field.getDeclaredAnnotation(Mapper.class);
            if(mapper==null){
                continue;
            }
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod)&&field.getType().isInterface()) {
                SqlSessionFactory sqlSessionFactory=(SqlSessionFactory)this.applicationContext.getBean(SqlSessionFactory.class.getName());
                if(this.isMapperBean(sqlSessionFactory,field.getType())){
                    MapperProxyFactory mapperProxyFactory=new MapperProxyFactory(field.getType());
                    SqlSession sqlSession=new SqlSessionTemplate(sqlSessionFactory);
                    fieldValue=mapperProxyFactory.newInstance(sqlSession);
                    InjectionMetadata.inject(bean,field,fieldValue);
                }
            }
        }
        return bean;
    }
    private boolean isMapperBean(SqlSessionFactory sqlSessionFactory, Class<?> cls){
        if(sqlSessionFactory==null||cls==null){
            return false;
        }
        if(sqlSessionFactory.getConfiguration().getMapperRegistry().hasMapper(cls)){
            return true;
        }else{
            return false;
        }
    }
}
