package com.jbosframework.boot.autoconfig;

import com.jbosframework.context.ApplicationContext;

/**
 * AutoConfigurationSelector
 * @author youfu.wang
 * @version 1.0
 */
public class AutoConfigurationSelector {
    public static final String AUTOCONFIGURATION_DATASOURCE="com.jbosframework.boot.autoconfig.jdbc.DataSourceAutoConfiguration";

    private ApplicationContext ctx;

    /**
     * 构造方法
     * @param ctx
     */
    public AutoConfigurationSelector(ApplicationContext ctx){
        this.ctx=ctx;
    }

}
