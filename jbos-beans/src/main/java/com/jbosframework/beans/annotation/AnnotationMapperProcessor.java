package com.jbosframework.beans.annotation;

import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.orm.mybatis.SqlSessionBeanUtils;
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

    public AnnotationMapperProcessor(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    public void process(Object obj){
        if (obj==null){
            return;
        }
        Class<?> cls=obj.getClass();
        Field[] fields=cls.getDeclaredFields();
        if(fields==null) {
            return;
        }
        InjectionMetadata injectionMetadata=new InjectionMetadata(this.beanFactory);
        for(int i=0;i<fields.length;i++) {
            Mapper mapperAnnotation=fields[i].getDeclaredAnnotation(Mapper.class);
            if(mapperAnnotation==null) {
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
            if(fields[i].getType().isInterface()) {
                SqlSessionFactory sqlSessionFactory=(SqlSessionFactory)this.beanFactory.getBean(SqlSessionFactory.class.getName());
                if(SqlSessionBeanUtils.isMapperBean(sqlSessionFactory,fields[i].getType())){
                    MapperProxyFactory mapperProxyFactory=new MapperProxyFactory(fields[i].getType());
                    SqlSession sqlSession=sqlSessionFactory.openSession(true);
                    fieldValue=mapperProxyFactory.newInstance(sqlSession);
                    injectionMetadata.inject(obj,fields[i],fieldValue);
                }
            }
        }
    }
}
