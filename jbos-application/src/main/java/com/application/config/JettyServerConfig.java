package com.application.config;

import com.jbosframework.beans.annotation.Bean;
import com.jbosframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class JettyServerConfig {

    @Bean(value="initJettyServer",initMethod = "startServer")
    public JettyServerService initJettyServer(){
        JettyServerService jettyServerService=new JettyServerService();
        return jettyServerService;
    }
}
