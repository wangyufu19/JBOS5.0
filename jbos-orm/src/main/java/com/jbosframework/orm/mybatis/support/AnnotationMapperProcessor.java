package com.jbosframework.orm.mybatis.support;

import com.jbosframework.core.Order;
import com.jbosframework.orm.mybatis.annotation.Mapper;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.orm.mybatis.SqlSessionTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/**
 * AnnotationMapperProcessor
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationMapperProcessor implements BeanPostProcessor {
    private static final Log log= LogFactory.getLog(AnnotationMapperProcessor.class);
    private BeanFactory beanFactory;
    private int order= Order.MIN;

    public AnnotationMapperProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    public void setOrder(int order){
        this.order=order;
    }
    public int getOrder() {
        return this.order;
    }
    public int compareTo(BeanPostProcessor beanPostProcessor) {
        return this.order - beanPostProcessor.getOrder();
    }
    /**
     * isMapperBean
     * @param sqlSessionFactory
     * @return
     */
    private boolean isMapperBean(SqlSessionFactory sqlSessionFactory,Class<?> cls){
        boolean bool=false;
        if(sqlSessionFactory==null||cls==null){
            return false;
        }
        if(sqlSessionFactory.getConfiguration().getMapperRegistry().hasMapper(cls)){
            bool=true;
        }
        return bool;
    }
    public Object process(Object obj) {
        Object target=obj;
        if (obj==null){
            return null;
        }
        Class<?> cls=target.getClass();
        Field[] fields=cls.getDeclaredFields();
        if(fields==null) {
            return target;
        }
        InjectionMetadata injectionMetadata=new InjectionMetadata();
        for(int i=0;i<fields.length;i++) {
            Mapper mapperAnnotation=fields[i].getDeclaredAnnotation(Mapper.class);
            if(mapperAnnotation==null) {
                continue;
            }
            //校验字段注解是否用在了static方法上
            if (Modifier.isStatic(fields[i].getModifiers())) {
                if (log.isWarnEnabled()) {
                    log.warn("Field "+fields[i].getName()+"is not supported on static fields: " + fields[i].getName());
                }
                return target;
            }
            Object fieldValue=null;
            if(fields[i].getType().isInterface()) {
                SqlSessionFactory sqlSessionFactory=(SqlSessionFactory)this.beanFactory.getBean(SqlSessionFactory.class.getName());
                if(this.isMapperBean(sqlSessionFactory,fields[i].getType())){
                    MapperProxyFactory mapperProxyFactory=new MapperProxyFactory(fields[i].getType());
                    SqlSession sqlSession=new SqlSessionTemplate(sqlSessionFactory);
                    fieldValue=mapperProxyFactory.newInstance(sqlSession);
                    injectionMetadata.inject(target,fields[i],fieldValue);
                }
            }
        }
        return target;
    }
}
