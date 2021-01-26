package com.jbosframework.boot.web.servlet;

import com.jbosframework.boot.web.TomcatWebServer;
import com.jbosframework.boot.web.WebServer;
import com.jbosframework.web.context.ContextLoaderServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

/**
 * TomcatServletWebServer
 * @author youfu.wang
 * @version 1.0
 */
public class TomcatServletWebServer extends AbstractServletWebServer{
    private String protocol="org.apache.coyote.http11.Http11NioProtocol";


    public TomcatServletWebServer(int port) {
        super(port);
    }
    public TomcatServletWebServer(int port,String contextPath) {
        super(port, contextPath);
    }
    public TomcatServletWebServer(int port,String baseDir, String contextPath) {
        super(port, baseDir,contextPath);
    }

    public WebServer getWebServer(){
        Tomcat tomcat=new Tomcat();
        tomcat.setPort(this.getPort());
        tomcat.setBaseDir(this.getBaseDir());
        tomcat.getHost().setAutoDeploy(false);
        Context context=tomcat.addContext("",this.getBaseDir());
        context.getServletContext().setAttribute(ContextLoaderServlet.APPLICATION_CONTEXT_ATTRIBUTE,this.getApplicationContext());

        WebContextRegistryBean webContextRegistryBean=new WebContextRegistryBean(context,this.getApplicationContext());
        webContextRegistryBean.setOrder(1000);
        webContextRegistryBean.initDispatcherServlet(this.getContextPath());
        this.getApplicationContext().addBeanBeforeProcessor(webContextRegistryBean);

        WebServer webServer=new TomcatWebServer(tomcat);
        return webServer;
    }
}
