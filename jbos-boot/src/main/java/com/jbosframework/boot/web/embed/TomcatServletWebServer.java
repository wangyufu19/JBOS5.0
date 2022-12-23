package com.jbosframework.boot.web.embed;

import com.jbosframework.boot.web.WebServer;
import com.jbosframework.boot.web.servlet.AbstractServletWebServer;
import com.jbosframework.boot.web.servlet.context.WebContext;
import com.jbosframework.boot.web.servlet.server.ServletContextInitializer;
import com.jbosframework.boot.web.servlet.server.ServletWebServerFactory;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.web.context.support.GenericWebApplicationContext;
import com.jbosframework.web.servlet.DispatcherServlet;
import com.jbosframework.web.servlet.WebInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

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

        return webServer;
    }
}
