package com.jbosframework.boot.autoconfig.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TomcatDataSourceProperties
 * @author youfu.wang
 * @version 1.0
 */
public class TomcatDataSourceProperties extends DataSourceProperties{
    private static final Log log= LogFactory.getLog(TomcatDataSourceProperties.class);

    /**
     * 加载数据源属性
     * @param properties
     */
    public void load(Object properties){
        log.info("******properties: "+properties);
    }
}
