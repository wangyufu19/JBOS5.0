package com.jbosframework.boot.web.embed;

import com.jbosframework.boot.web.servlet.context.WebContext;
import org.apache.catalina.Context;

public class TomcatWebContext implements WebContext {
    private Context context;

    public TomcatWebContext(Context context){
        this.context=context;
    }
    public void setAttribute(String name, Object value){
        context.getServletContext().setAttribute(name,value);
    }
}
