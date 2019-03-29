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
    public static boolean isSqlSessionBean(SqlSessionFactory sqlSessionFactory,Object obj){
        boolean bool=false;
        if(sqlSessionFactory==null||obj==null){
            return bool;
        }
        if(sqlSessionFactory.getConfiguration().getMapperRegistry().hasMapper(obj.getClass())){
            bool=true;
        }
        return bool;
    }
}
