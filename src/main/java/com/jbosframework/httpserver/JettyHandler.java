package com.jbosframework.httpserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * JettyHttpServer
 * @author youfu.wang
 * @date 2019-10-22
 */
public class JettyHandler extends AbstractHandler{
    private static Log log= LogFactory.getLog(JettyHandler.class);

    /**
     * 处理HTTP请求并且响应相应的资源
     * @param s
     * @param jRequest
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(String s,
                       Request jRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {
        log.info("******request method: "+request.getMethod());
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello World</h1>");
        jRequest.setHandled(true);
    }
}
