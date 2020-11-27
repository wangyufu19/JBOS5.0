package com.jbosframework.boot.web.servlet;

import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import java.util.Map;

/**
 * WebContextRegistryBean
 * @author youfu.wang
 * @version 5.0
 * @date 2020-11-27
 */
public class WebContextRegistryBean implements BeanPostProcessor {
    public static final Log logger= LogFactory.getLog(WebContextRegistryBean.class);
    private Context context;

    public WebContextRegistryBean(Context context){
        this.context=context;
    }

    public void initDispatcherServlet(String contextPath){
        Wrapper wrapper = context.createWrapper();
        wrapper.setName(DispatcherServlet.class.getSimpleName());
        wrapper.setServletClass(DispatcherServlet.class.getName());
        context.addChild(wrapper);
        context.addServletMappingDecoded(contextPath+"/*", DispatcherServlet.class.getSimpleName());
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
        }else if(obj instanceof ServletRegistryBean){
            ServletRegistryBean servletRegistryBean=(ServletRegistryBean)obj;
            Wrapper wrapper = context.createWrapper();
            wrapper.setName(servletRegistryBean.getName());
            wrapper.setServletClass(servletRegistryBean.getServlet().getClass().getName());
            context.addChild(wrapper);
            context.addServletMappingDecoded(servletRegistryBean.getUrlPattern(), servletRegistryBean.getName());
            logger.info("******Registry servlet "+servletRegistryBean.getServlet().getClass().getName());
        }
    }
}
