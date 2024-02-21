package com.jbosframework.boot.web.embed;

import com.jbosframework.boot.web.WebServer;
import com.jbosframework.boot.web.servlet.AbstractServletWebServer;
import com.jbosframework.boot.web.servlet.FilterRegistryBean;
import com.jbosframework.boot.web.servlet.ServletRegistryBean;
import com.jbosframework.boot.web.servlet.server.ServletWebServerFactory;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.utils.ObjectUtils;
import com.jbosframework.web.context.support.GenericWebApplicationContext;
import com.jbosframework.web.filter.DelegatingFilterProxy;
import com.jbosframework.web.servlet.DispatcherServlet;
import com.jbosframework.web.servlet.WebInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.Filter;
import java.util.Map;

/**
 * TomcatServletWebServer
 * @author youfu.wang
 * @version 1.0
 */
public class TomcatServletWebServer extends AbstractServletWebServer implements ServletWebServerFactory {
    private String protocol="org.apache.coyote.http11.Http11NioProtocol";
    private ConfigurableApplicationContext applicationContext;


    public TomcatServletWebServer(ConfigurableApplicationContext applicationContext) {
        super(8080);
        this.applicationContext=applicationContext;
        this.setApplicationContext(applicationContext);
    }
    public WebServer getWebServer(WebInitializer webInitializer){
        String classPath=String.valueOf(applicationContext.getEnvironment().getProperty(GenericWebApplicationContext.CTX_PROPERTY_CLASSPATH));
        if(applicationContext.getEnvironment().getProperty(GenericWebApplicationContext.CTX_PROPERTY_SERVER_PORT)!=null){
            int port=Integer.parseInt(applicationContext.getEnvironment().getProperty(GenericWebApplicationContext.CTX_PROPERTY_SERVER_PORT));
            this.setPort(port);
        }
        if(applicationContext.getEnvironment().getProperty(GenericWebApplicationContext.CTX_PROPERTY_SERVER_CONTEXTPATH)!=null) {
            String contextPath=String.valueOf(applicationContext.getEnvironment().getProperty(GenericWebApplicationContext.CTX_PROPERTY_SERVER_CONTEXTPATH));
            this.setContextPath(contextPath);
        }
        Tomcat tomcat=new Tomcat();
        tomcat.setPort(this.getPort());
        tomcat.setBaseDir(this.getBaseDir());
        tomcat.getHost().setAutoDeploy(false);
        WebServer webServer=new TomcatWebServer(applicationContext,tomcat);
        Context context=tomcat.addContext("",this.getBaseDir());

        webServer.setWebContext(new TomcatWebContext(context));
        webServer.getWebContext().addServletMappingDecoded(this.getContextPath(),DispatcherServlet.class);
        String[] filterRegistryBeans=this.applicationContext.getBeanNamesOfType(FilterRegistryBean.class);
        String[] servletRegistryBeans=this.applicationContext.getBeanNamesOfType(ServletRegistryBean.class);
        if(!ObjectUtils.isEmpty(filterRegistryBeans)) {
            for(String filterRegistryBean:filterRegistryBeans){
                Object bean=this.applicationContext.getBean(filterRegistryBean);
                this.addFilterRegistryBean(context,bean);
            }
        }
        if(!ObjectUtils.isEmpty(servletRegistryBeans)) {
            for(String servletRegistryBean:servletRegistryBeans){
                Object bean=this.applicationContext.getBean(servletRegistryBean);
                this.addServletRegistryBean(context,bean);
            }
        }
        return webServer;
    }
    private void addFilterRegistryBean(Context context,Object bean){
        if(bean instanceof FilterRegistryBean){
            FilterRegistryBean filterRegistryBean=(FilterRegistryBean)bean;
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
        }
    }
    private void addServletRegistryBean(Context context,Object bean){
        if(bean instanceof ServletRegistryBean){
            ServletRegistryBean servletRegistryBean=(ServletRegistryBean)bean;
            Wrapper wrapper = context.createWrapper();
            wrapper.setName(servletRegistryBean.getName());
            wrapper.setServletClass(servletRegistryBean.getServlet().getClass().getName());
            context.addChild(wrapper);
            context.addServletMappingDecoded(servletRegistryBean.getUrlPattern(), servletRegistryBean.getName());
        }
    }
}
