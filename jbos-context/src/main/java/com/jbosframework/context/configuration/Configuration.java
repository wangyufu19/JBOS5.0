package com.jbosframework.context.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Configuration
 * @author youfu.wang
 * @version 5.0
 */
public abstract class Configuration {
    private static Log log= LogFactory.getLog(Configuration.class);
    /**
     * 加载配置属性
     */
    public abstract void load();
    /**
     * 得到上下文属性
     * @param name
     * @return
     */
    public abstract Object getContextProperty(String name);
    /**
     * 清除属性内存
     */
    public abstract void clear();
}
