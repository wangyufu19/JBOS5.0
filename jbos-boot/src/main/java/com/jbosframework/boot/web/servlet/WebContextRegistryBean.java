package com.jbosframework.boot.web.servlet;

import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.web.filter.DelegatingFilterProxy;
import com.jbosframework.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.Filter;
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
    private ApplicationContext applicationContext;

    public WebContextRegistryBean(Context context,ApplicationContext applicationContext){
        this.context=context;
        this.applicationContext=applicationContext;
    }

    public void initDispatcherServlet(String contextPath){
        Wrapper wrapper = context.createWrapper();
        wrapper.setName(DispatcherServlet.class.getSimpleName());
        wrapper.setServletClass(DispatcherServlet.class.getName());
        context.addChild(wrapper);
        context.addServletMappingDecoded(contextPath+"/*", DispatcherServlet.class.getSimpleName());
    }
    public Object process(Object obj){
        Object target=obj;
        if(target instanceof FilterRegistryBean){
            FilterRegistryBean filterRegistryBean=(FilterRegistryBean)target;
            FilterDef filterDef = new FilterDef();
            String filterName=filterRegistryBean.getName();
            Filter filter=filterRegistryBean.getFilter();
            if(filter instanceof DelegatingFilterProxy){
                ((DelegatingFilterProxy)filter).setApplicationContext(applicationContext);
                filterName=((DelegatingFilterProxy)filter).getBeanName();
            }
            filterDef.setFilterName(filterName);
            filterDef.setFilter(filter);
            Map<String,String> initParameters = filterRegistryBean.getInitParameters();
            for(Map.Entry<String,String> entry:initParameters.entrySet()){
                filterDef.addInitParameter(entry.getKey(),entry.getValue());
            }
            context.addFilterDef(filterDef);

            FilterMap mapping = new FilterMap();
            mapping.setFilterName(filterName);
            mapping.addURLPattern(filterRegistryBean.getUrlPattern());
            context.addFilterMap(mapping);

            logger.info("******Registry filter "+filterRegistryBean.getFilter().getClass().getName());
        }else if(target instanceof ServletRegistryBean){
            ServletRegistryBean servletRegistryBean=(ServletRegistryBean)target;
            Wrapper wrapper = context.createWrapper();
            wrapper.setName(servletRegistryBean.getName());
            wrapper.setServletClass(servletRegistryBean.getServlet().getClass().getName());
            context.addChild(wrapper);
            context.addServletMappingDecoded(servletRegistryBean.getUrlPattern(), servletRegistryBean.getName());
            logger.info("******Registry servlet "+servletRegistryBean.getServlet().getClass().getName());
        }
        return target;
    }
}
