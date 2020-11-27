package com.jbosframework.boot.web;

import com.jbosframework.boot.web.servlet.TomcatServletWebServer;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.web.context.WebAnnotationApplicationContext;
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
    public Class<?> jbosBootClass;
    private WebServer webServer;
    private int port=8080;

    public JBOSWebApplicationContext(ApplicationContext applicationContext,Class<?> jbosBootClass){
        this.applicationContext=applicationContext;
        this.jbosBootClass=jbosBootClass;
    }
    public void onStartup(){
        String contextPath="";
        String classPath=applicationContext.getContextConfiguration().getConfigClassPath();
        if(applicationContext.getContextConfiguration().getContextProperty(CTX_PROPERTY_SERVER_TOMCAT_PORT)!=null){
            port=Integer.parseInt(String.valueOf(applicationContext.getContextConfiguration().getContextProperty(CTX_PROPERTY_SERVER_TOMCAT_PORT)));
        }
        if(applicationContext.getContextConfiguration().getContextProperty(CTX_PROPERTY_SERVER_TOMCAT_CONTEXTPATH)!=null) {
            contextPath=String.valueOf(applicationContext.getContextConfiguration().getContextProperty(CTX_PROPERTY_SERVER_TOMCAT_CONTEXTPATH));
        }
        try{
            TomcatServletWebServer tomcatServletWebServer=new TomcatServletWebServer(port,classPath,contextPath);
            tomcatServletWebServer.setApplicationContext(applicationContext);
            webServer=tomcatServletWebServer.getWebServer();
            WebAnnotationApplicationContext webAnnotationApplicationContext=new WebAnnotationApplicationContext(applicationContext);
            webAnnotationApplicationContext.initWebApplicationContext(jbosBootClass);
            webServer.start();
            logger.info("Tomcat started on port:"+this.port+" with com.jbosframework.boot.context path '"+contextPath+"'");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}