package com.jbosframework.context.support;
import com.jbosframework.context.configuration.Configuration;
/**
 * ContextInitializer
 * @author youfu.wang
 * @version 1.0
 */
public class ContextInitializer {

    private Configuration configuration;

    public ContextInitializer(){

    }
    /**
     * 构造方法
     * @param configuration
     */
    public ContextInitializer(Configuration configuration){
        this.configuration=configuration;
    }

    /**
     * 得到上下文配置
     * @return
     */
    public Configuration getContextConfiguration(){
        return configuration;
    }


}
