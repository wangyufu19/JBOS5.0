package com.jbosframework.orm.mybatis.support;

import com.jbosframework.beans.factory.DependencyFactory;
import com.jbosframework.orm.mybatis.annotation.Mapper;
import com.jbosframework.beans.config.InjectionMetadata;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.orm.mybatis.SqlSessionTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.lang.reflect.Field;
/**
 * AnnotationMapperProcessor
 * @author youfu.wang
 * @version 1.0
 */
public class MapperDependencyFactory implements DependencyFactory {
    private static final Log log= LogFactory.getLog(MapperDependencyFactory.class);
    private BeanFactory beanFactory;


    public MapperDependencyFactory(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }


    /**
     * isMapperBean
     * @param sqlSessionFactory
     * @return
     */
    private boolean isMapperBean(SqlSessionFactory sqlSessionFactory,Class<?> cls){
        if(sqlSessionFactory==null||cls==null){
            return false;
        }
        if(sqlSessionFactory.getConfiguration().getMapperRegistry().hasMapper(cls)){
            return true;
        }else{
            return false;
        }
    }
    public void autowireDependency(Object existingBean,Field field) {
        InjectionMetadata injectionMetadata=new InjectionMetadata();
        Mapper mapperAnnotation=field.getDeclaredAnnotation(Mapper.class);
        if(mapperAnnotation==null){
            return;
        }
        Object fieldValue=null;
        if(field.getType().isInterface()) {
            SqlSessionFactory sqlSessionFactory=(SqlSessionFactory)this.beanFactory.getBean(SqlSessionFactory.class.getName());
            if(this.isMapperBean(sqlSessionFactory,field.getType())){
                MapperProxyFactory mapperProxyFactory=new MapperProxyFactory(field.getType());
                SqlSession sqlSession=new SqlSessionTemplate(sqlSessionFactory);
                fieldValue=mapperProxyFactory.newInstance(sqlSession);
                injectionMetadata.inject(existingBean,field,fieldValue);
            }
        }
    }
}
