package com.jbosframework.boot.web;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TomcatWebServer
 * @author youfu.wang
 * @version 1.0
 */
public class TomcatWebServer implements WebServer{
    public static final Log logger= LogFactory.getLog(TomcatWebServer.class);
    private Tomcat tomcat;

    public TomcatWebServer(Tomcat tomcat){
        this.tomcat=tomcat;
    }
    public void start() throws LifecycleException {
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }
    public int getPort() {
        Connector connector=tomcat.getConnector();
        return connector!=null?connector.getLocalPort():0;
    }
}
