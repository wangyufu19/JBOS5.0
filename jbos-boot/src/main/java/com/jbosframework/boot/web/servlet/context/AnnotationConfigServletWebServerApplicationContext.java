package com.jbosframework.boot.web.servlet.context;

import com.jbosframework.boot.web.WebServer;
import com.jbosframework.boot.web.embed.TomcatServletWebServer;
import com.jbosframework.boot.web.servlet.server.ServletContextInitializer;
import com.jbosframework.boot.web.servlet.server.ServletWebServerFactory;
import com.jbosframework.context.ApplicationContextException;
import com.jbosframework.context.annotation.AnnotatedBeanDefinitionReader;
import com.jbosframework.context.annotation.AnnotationConfigRegistry;
import com.jbosframework.context.annotation.ClassPathBeanDefinitionScanner;
import com.jbosframework.web.context.support.GenericWebApplicationContext;
import org.apache.catalina.LifecycleException;

import javax.servlet.ServletContext;


/**
 * AnnotationConfigServletWebServerApplicationContext
 * @author youfu.wang
 */
public class AnnotationConfigServletWebServerApplicationContext extends GenericWebApplicationContext implements AnnotationConfigRegistry {
    private final AnnotatedBeanDefinitionReader reader;
    private final ClassPathBeanDefinitionScanner scanner;
    private volatile WebServer webServer;
    private ServletContextInitializer initializer;

    public AnnotationConfigServletWebServerApplicationContext(){
        this.reader = new AnnotatedBeanDefinitionReader(this);
        this.scanner = new ClassPathBeanDefinitionScanner(this);
    }
    public void registry(Class<?>... classes) {

    }

    public void scan(String... basePackages) {

    }
    protected void onRefresh() {
        super.onRefresh();
        try {
            createWebServer();
        } catch (Throwable ex) {
            throw new ApplicationContextException("Unable to start web server", ex);
        }
    }
    private void createWebServer() throws LifecycleException {
        WebServer webServer = this.webServer;
        ServletContext servletContext = getServletContext();
        if (webServer == null && servletContext == null) {
            ServletWebServerFactory factory = new TomcatServletWebServer(this);
            this.webServer = factory.getWebServer(initializer);
            this.webServer.start();
        }
    }

}