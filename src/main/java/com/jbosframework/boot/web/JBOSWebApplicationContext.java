package com.jbosframework.boot.web;

import com.jbosframework.context.ApplicationContext;
import com.jbosframework.web.context.ContextLoaderServlet;
import com.jbosframework.web.servlet.DispatcherServlet;
import com.jbosframework.web.servlet.WebInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * JBOSWebApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public class JBOSWebApplicationContext {
    public static final Log logger= LogFactory.getLog(JBOSWebApplicationContext.class);
    public static final String CTX_PROPERTY_SERVER_APPLICATION="server.application";
    public static final String CTX_PROPERTY_SERVER_TOMCAT_PORT="server.tomcat.port";
    public static final String CTX_PROPERTY_SERVER_TOMCAT_CONTEXTPATH="server.tomcat.contextPath";
    private ApplicationContext applicationContext;
    private WebServer webServer;
    private Context ctx;
    private int port=8080;

    public JBOSWebApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    public void onStartup(){
        String contextPath="";
        String classPath=applicationContext.getContextConfiguration().getConfigClassPath();
        Tomcat tomcat=new Tomcat();
        if(applicationContext.getContextConfiguration().getContextProperty(CTX_PROPERTY_SERVER_TOMCAT_PORT)!=null){
            port=Integer.parseInt(String.valueOf(applicationContext.getContextConfiguration().getContextProperty(CTX_PROPERTY_SERVER_TOMCAT_PORT)));
        }
        if(applicationContext.getContextConfiguration().getContextProperty(CTX_PROPERTY_SERVER_TOMCAT_CONTEXTPATH)!=null) {
            contextPath=String.valueOf(applicationContext.getContextConfiguration().getContextProperty(CTX_PROPERTY_SERVER_TOMCAT_CONTEXTPATH));
        }
        tomcat.setPort(port);
        tomcat.setBaseDir(classPath);
        ctx=tomcat.addContext("",classPath);

        try{
            webServer=new TomcatWebServer(tomcat);
            logger.info("Tomcat started on port:"+this.port+" with context path '"+contextPath+"'");
            ctx.getServletContext().setAttribute(ContextLoaderServlet.APPLICATION_CONTEXT_ATTRIBUTE,applicationContext);
            tomcat.addServlet(ctx,DispatcherServlet.class.getSimpleName(), new DispatcherServlet());
            ctx.addServletMappingDecoded(contextPath+"/*", DispatcherServlet.class.getSimpleName());
            webServer.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}