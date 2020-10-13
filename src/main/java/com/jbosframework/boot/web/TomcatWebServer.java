package com.jbosframework.boot.web;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

/**
 * TomcatWebServer
 * @author youfu.wang
 * @version 1.0
 */
public class TomcatWebServer implements WebServer{
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
