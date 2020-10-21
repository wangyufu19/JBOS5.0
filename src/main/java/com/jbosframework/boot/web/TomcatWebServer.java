package com.jbosframework.boot.web;

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
public class TomcatWebServer implements WebServer{
    public static final Log logger= LogFactory.getLog(TomcatWebServer.class);
    private static final AtomicInteger containerCounter=new AtomicInteger(-1);
    private final Tomcat tomcat;

    public TomcatWebServer(Tomcat tomcat){
        this.tomcat=tomcat;
    }
    public void start() throws LifecycleException {
        tomcat.getConnector();
        tomcat.start();
        this.startDaemonAwaitThread();
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
}
