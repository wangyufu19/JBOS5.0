package com.jbosframework.boot.web.embed;

import com.jbosframework.boot.web.servlet.context.WebContext;
import com.jbosframework.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;

public class TomcatWebContext implements WebContext {
    private Context context;

    public TomcatWebContext(Context context){
        this.context=context;
    }
    public void setAttribute(String name, Object value){
        context.getServletContext().setAttribute(name,value);
    }
    public void addServletMappingDecoded(String contextPath,Class servletClass){
        Wrapper wrapper = context.createWrapper();
        wrapper.setName(servletClass.getSimpleName());
        wrapper.setServletClass(servletClass.getName());
        context.addChild(wrapper);
        context.addServletMappingDecoded(contextPath+"/*", servletClass.getSimpleName());
    }


}
