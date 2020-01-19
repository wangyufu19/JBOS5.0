package com.jbosframework.httpserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

/**
 * JettyHttpServer
 * @author youfu.wang
 * @date 2019-10-22
 */
public class JettyHttpServer {
    private int _port=8080;
    private Server _server;
    private JettyHandler _jettyHandler;
    /**
     * 构造方法
     * @param port
     */
    public JettyHttpServer(int port){
        this._port=port;
    }
    /**
     * 设置处理器
     * @param jettyHandler
     */
    public void setJettyHandler(JettyHandler jettyHandler){
        this._jettyHandler=jettyHandler;
    }

    /**
     * 启动一个HTTP服务
     * @throws Exception
     */
    public void start() throws Exception {
        _server = new Server();
        // HTTP connector
        ServerConnector http = new ServerConnector(_server);
        http.setHost("localhost");
        http.setPort(_port);
        http.setIdleTimeout(1);
        // Set the connector
        _server.addConnector(http);
        // Set a handler
        _server.setHandler(_jettyHandler);
        _server.start();
        _server.join();
    }

    /**
     * 停止HTTP服务
     * @throws Exception
     */
    public void stop() throws Exception {
        if(_server!=null){
            _server.stop();
        }
    }

    public static void main(String[] args) throws Exception {
        JettyHttpServer httpServer=new JettyHttpServer(8080);
        httpServer.setJettyHandler(new JettyHandler());
        httpServer.start();
    }
}
