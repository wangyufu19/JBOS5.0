package com.jbosframework.transaction.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * TransactionSynchronizationManager
 * @author youfu.wang
 * @version 5.0
 */
public class TransactionSynchronizationManager {
    private static final Log log= LogFactory.getLog(TransactionSynchronizationManager.class);
    /**
     * 数据源本地线程副本变量
     */
    private static ThreadLocal<Map<Object, Object>> dataSources=new ThreadLocal<Map<Object,Object>>();

    public static Object getConnectionHolder(DataSource dataSource){
        Map<Object, Object> currentDataSource=dataSources.get();
        if(currentDataSource!=null){
            return currentDataSource.get(getDataSourceKey(dataSource));
        }else{
            return null;
        }
    }
    public static void bindConnectionHolder(DataSource dataSource,Object connectionHolder){
        Map<Object, Object> map=new HashMap<Object, Object>();
        map.put(getDataSourceKey(dataSource),connectionHolder);
        dataSources.set(map);
    }
    private static String getDataSourceKey(Object obj){
        return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
    }
    public static void removeConnectionHolder(DataSource dataSource){
        dataSources.remove();
    }
}
