package com.jbosframework.boot.web.servlet;

import com.jbosframework.beans.config.BeanDefinition;
import com.jbosframework.beans.config.BeanPostProcessor;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.core.Ordered;
import com.jbosframework.schedule.annotation.async.AsyncBeanPostProcessor;
import com.jbosframework.web.filter.DelegatingFilterProxy;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.Filter;
import java.util.Map;

public class WebContextBeanPostProcessor implements BeanPostProcessor, Ordered {
    private static final Log logger= LogFactory.getLog(AsyncBeanPostProcessor.class);
    private Context context;
    private ConfigurableApplicationContext applicationContext;
    private int order=Ordered.HIGHEST_PRECEDENCE+100;

    public WebContextBeanPostProcessor(ConfigurableApplicationContext applicationContext,Context context){
        this.applicationContext=applicationContext;
        this.context=context;
    }
    public int getOrder() {
        return this.order;
    }

    public Object postProcessBeforeInitialization(Object bean, BeanDefinition beanDefinition){
        Object target=bean;
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

        }else if(target instanceof ServletRegistryBean){
            ServletRegistryBean servletRegistryBean=(ServletRegistryBean)target;
            Wrapper wrapper = context.createWrapper();
            wrapper.setName(servletRegistryBean.getName());
            wrapper.setServletClass(servletRegistryBean.getServlet().getClass().getName());
            context.addChild(wrapper);
            context.addServletMappingDecoded(servletRegistryBean.getUrlPattern(), servletRegistryBean.getName());
        }
        return bean;
    }
}
