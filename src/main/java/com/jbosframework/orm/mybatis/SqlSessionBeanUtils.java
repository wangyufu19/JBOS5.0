package com.jbosframework.orm.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * SqlSessionBeanUtils
 * @author youfu.wang
 * @version 1.0
 */
public class SqlSessionBeanUtils {
    /**
     * isSqlSessionBean
     * @param sqlSessionFactory
     * @return
     */
    public static boolean isMapperBean(SqlSessionFactory sqlSessionFactory,Class<?> cls){
        boolean bool=false;
        if(sqlSessionFactory==null||cls==null){
            return bool;
        }
        if(sqlSessionFactory.getConfiguration().getMapperRegistry().hasMapper(cls)){
            bool=true;
        }
        return bool;
    }
}
