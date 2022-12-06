package com.jbosframework.boot.web.servlet;

import com.jbosframework.context.ApplicationContext;

/**
 * AbstractServletWebServer
 * @author youfu.wang
 * @version 1.0
 */
public class AbstractServletWebServer {
    private int port=8080;
    private String baseDir="";
    private String contextPath="";
    private ApplicationContext applicationContext;
    public AbstractServletWebServer(){

    }
    public AbstractServletWebServer(int port){
        this.port=port;
    }
    public AbstractServletWebServer(int port,String contextPath){
        this.port=port;
        this.contextPath=contextPath;
    }
    public AbstractServletWebServer(int port,String baseDir,String contextPath){
        this.port=port;
        this.baseDir=baseDir;
        this.contextPath=contextPath;
    }
    public void setPort(int port){
        this.port=port;
    }
    public int getPort(){
        return port;
    }
    public String getBaseDir(){
        return baseDir;
    }
    public void setContextPath(String contextPath){
        this.contextPath=contextPath;
    }
    public String getContextPath(){
        return contextPath;
    }
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    public ApplicationContext getApplicationContext(){
        return this.applicationContext;
    }
}
