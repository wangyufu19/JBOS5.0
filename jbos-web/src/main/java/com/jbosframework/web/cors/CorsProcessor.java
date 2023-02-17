package com.jbosframework.web.cors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CorsProcessor {
    boolean processRequest( HttpServletRequest request, HttpServletResponse response) throws IOException;
}