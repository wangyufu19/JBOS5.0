package com.jbosframework.boot.web.embed;

import com.jbosframework.boot.web.WebServer;
import com.jbosframework.boot.web.servlet.context.WebContext;
import com.jbosframework.context.ConfigurableApplicationContext;
import com.jbosframework.web.context.ContextLoaderServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TomcatWebServer
 * @author youfu.wang
 * @version 1.0
 */
public class TomcatWebServer implements WebServer {
    public static final Log logger= LogFactory.getLog(TomcatWebServer.class);
    private static final AtomicInteger containerCounter=new AtomicInteger(-1);
    private final Tomcat tomcat;
    private ConfigurableApplicationContext applicationContext;
    private WebContext webContext;

    public TomcatWebServer(ConfigurableApplicationContext applicationContext,Tomcat tomcat){
        this.applicationContext=applicationContext;
        this.tomcat=tomcat;
    }
    public void start() throws LifecycleException {
        tomcat.start();
        this.startDaemonAwaitThread();
        logger.info("Tomcat started on port:"+this.tomcat.getConnector().getPort()+" with com.jbosframework.boot.context path '"+this.tomcat.getConnector()+"'");
        this.webContext.setAttribute(ContextLoaderServlet.APPLICATION_CONTEXT_ATTRIBUTE,this.applicationContext);
    }
    private void startDaemonAwaitThread(){
        Thread t=new Thread("container-"+containerCounter.get()){
            public void run(){
                tomcat.getServer().await();
            }
        };
        t.setContextClassLoader(this.getClass().getClassLoader());
        t.setDaemon(false);
        t.start();
    }
    public void stop() throws LifecycleException {
        tomcat.stop();
    }
    public int getPort() {
        Connector connector=tomcat.getConnector();
        return connector!=null?connector.getLocalPort():0;
    }
    public void setWebContext(WebContext webContext){
        this.webContext=webContext;
    }
    public WebContext getWebContext(){
        return webContext;
    }
}
