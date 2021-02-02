package com.application.sys.service;

import com.application.common.httpserver.JettyHttpServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JettyServerService {

    public void startServer()  {
        Thread t=new Thread(){
            public void run(){
                JettyHttpServer jettyHttpServer=new JettyHttpServer(9081);
                jettyHttpServer.start();
                log.info("******jettyHttpServer: "+jettyHttpServer);
            }
        };
        t.start();
    }

}
