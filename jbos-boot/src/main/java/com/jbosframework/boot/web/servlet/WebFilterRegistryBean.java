package com.jbosframework.boot.web.servlet;

import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.web.filter.CharacterEncodingFilter;
import org.apache.catalina.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * FilterRegistryBeanProcessor
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-27
 */
public class WebFilterRegistryBean implements BeanPostProcessor {
    public static final Log logger= LogFactory.getLog(WebFilterRegistryBean.class);


    private Context context;

    public WebFilterRegistryBean(Context context){
        this.context=context;
    }
    public void process(Object obj){
        if(obj instanceof FilterRegistryBean){
            FilterRegistryBean filterRegistryBean=(FilterRegistryBean)obj;
            FilterDef filterDef = new FilterDef();
            filterDef.setFilterName(filterRegistryBean.getName());
            filterDef.setFilter(filterRegistryBean.getFilter());
            Map<String,String> initParameters = filterRegistryBean.getInitParameters();
            for(Map.Entry<String,String> entry:initParameters.entrySet()){
                filterDef.addInitParameter(entry.getKey(),entry.getValue());
            }
            context.addFilterDef(filterDef);

            FilterMap mapping = new FilterMap();
            mapping.setFilterName(filterRegistryBean.getName());
            mapping.addURLPattern(filterRegistryBean.getUrlPattern());
            context.addFilterMap(mapping);

            logger.debug("******Registry filter "+filterRegistryBean.getFilter().getClass().getName());
        }
    }
}
