package com.application.config;

import com.application.common.httpserver.JettyHttpServer;
import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

//@Configuration
@Slf4j
public class JettyServerConfig {
    private JettyHttpServer jettyHttpServer;

    public void startServer() throws Exception {
        if(jettyHttpServer!=null){
            jettyHttpServer.start();
        }
    }
    @Bean(value="jettyHttpServer",initMethod="startServer")
    public JettyHttpServer initJettyServer(){
        jettyHttpServer=new JettyHttpServer(9081);
        log.info("******jettyHttpServer: "+jettyHttpServer);
        return jettyHttpServer;
    }
}
