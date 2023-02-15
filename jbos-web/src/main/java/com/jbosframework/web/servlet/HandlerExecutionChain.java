package com.jbosframework.web.servlet;

import com.jbosframework.core.Nullable;
import com.jbosframework.utils.ObjectUtils;
import com.jbosframework.web.mvc.ModelAndView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


public class HandlerExecutionChain {
    public static final Log logger= LogFactory.getLog(HandlerExecutionChain.class);
    private HttpServletRequest request;
    private HttpServletResponse response;
    @Nullable
    private List<HandlerInterceptor> interceptorList;
    private int interceptorIndex;


    public HandlerExecutionChain(HttpServletRequest request,HttpServletResponse response){
        this.request=request;
        this.response=response;
        this.interceptorIndex=-1;
    }
    public void addInterceptor(HandlerInterceptor interceptor) {
        if(this.interceptorList==null){
            this.interceptorList = new ArrayList();
        }
        this.interceptorList.add(interceptor);
    }
    public HandlerInterceptor[] getInterceptors() {
        if (this.interceptorList != null) {
            return this.interceptorList.toArray(new HandlerInterceptor[0]);
        }
        return new HandlerInterceptor[0];
    }

    public boolean applyPreHandle() throws Exception {
        HandlerInterceptor[] interceptors = this.getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for(int i = 0; i < interceptors.length; this.interceptorIndex = i++) {
                HandlerInterceptor interceptor = interceptors[i];
                if (!interceptor.preHandle(request, response, this)) {
                    this.triggerAfterCompletion((Exception)null);
                    return false;
                }
            }
        }
        return true;
    }
    public void applyPostHandle(@Nullable ModelAndView mv) throws Exception {
        HandlerInterceptor[] interceptors = this.getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for(int i = interceptors.length - 1; i >= 0; --i) {
                HandlerInterceptor interceptor = interceptors[i];
                interceptor.postHandle(request, response, this, mv);
            }
        }
    }

    public void triggerAfterCompletion(@Nullable Exception ex) throws Exception {
        HandlerInterceptor[] interceptors = this.getInterceptors();
        if (!ObjectUtils.isEmpty(interceptors)) {
            for(int i = this.interceptorIndex; i >= 0; --i) {
                HandlerInterceptor interceptor = interceptors[i];
                try {
                    interceptor.afterCompletion(request, response, this, ex);
                } catch (Throwable var8) {
                    logger.error("HandlerInterceptor.afterCompletion threw exception", var8);
                }
            }
        }
    }
}
