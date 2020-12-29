package com.jbosframework.transaction;

import com.jbosframework.jdbc.datasource.ConnectionHolder;
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
    private static ThreadLocal<Map<DataSource, ConnectionHolder>> dataSources=new ThreadLocal<Map<DataSource,ConnectionHolder>>();

    public static ConnectionHolder getConnectionHolder(DataSource dataSource){
        Map<DataSource, ConnectionHolder> currentDataSource=dataSources.get();
        if(currentDataSource!=null){
            return currentDataSource.get(dataSource);
        }else{
            return null;
        }
    }
    public static void bindConnectionHolder(DataSource dataSource,ConnectionHolder connectionHolder){
        Map<DataSource, ConnectionHolder> map=new HashMap<DataSource, ConnectionHolder>();
        map.put(dataSource,connectionHolder);
        dataSources.set(map);
    }
    public static void removeConnectionHolder(DataSource dataSource){
        dataSources.remove();
    }
}
