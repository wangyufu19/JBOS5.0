package com.jbosframework.web.mvc.dispatcher;

import java.lang.annotation.Annotation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import com.jbosframework.beans.config.GenericBeanDefinition;
import com.jbosframework.beans.config.MethodMetadata;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.utils.JBOSClassCaller;
import com.jbosframework.web.mvc.annotation.ResponseBody;
import com.jbosframework.web.utils.WebUtils;
import com.jbosframework.web.mvc.annotation.RequestMethod;
import com.jbosframework.web.mvc.annotation.WebAnnotationBean;
import com.jbosframework.web.mvc.data.Representation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DispatchHandler
 * @author youfu.wang
 * @version 1.0
 */
public class DispatchHandler {
	public static final Log logger= LogFactory.getLog(DispatchHandler.class);
	public static final String DEFAULT_REQUEST_MAPPING="handleRequest";
	private ApplicationContext applicationContext;
	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Dispatcher dispatcher;
	/**
	 * 构造方法
	 * @param request
	 * @param response
	 */
	public DispatchHandler(HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
		dispatcher=new Dispatcher(this.request,this.response);
	}	
	/**
	 * 设置ServletContext
	 * @param servletContext
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	/**
	 * 设置ApplicationContext
	 * @param applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext){
		this.applicationContext=applicationContext;
	}
	/**
	 * 处理分发
	 * @throws Exception
	 */
	public void handle() throws Exception{
		Object controller=null;
		String requestUri = WebUtils.getRequestPath(request);
		controller=this.getRequestController(applicationContext,requestUri);
		if(controller==null){			
			dispatcher.doDispatcher404(requestUri);
			return;
		}				
		if(controller instanceof GenericBeanDefinition){
			//使用注解加载配置的IoC对象	
			WebAnnotationBean webAnnotationBean=(WebAnnotationBean)controller;
			String requestMethod=request.getMethod().toUpperCase();
			boolean bool=false;
			if(webAnnotationBean.getRequestMethod()!=null){
				RequestMethod[] beanRequestMethod=webAnnotationBean.getRequestMethod();
				for(int i=0;i<beanRequestMethod.length;i++){
					if(requestMethod.equals(beanRequestMethod[i].name())){
						this.doHandle(requestUri,applicationContext,webAnnotationBean);
						bool=true;
						break;
					}else{
						bool=false;
					}
				}				
			}
			if(!bool){
				dispatcher.doDispatcher404(requestUri);
				return;
			}
		}
	}
	/**
	 * 处理请求
	 * @param applicationContext
	 * @param webAnnotationBean
	 * @return
	 * @throws Exception
	 */
	private void doHandle(String requestUri,ApplicationContext applicationContext,WebAnnotationBean webAnnotationBean) throws Exception{
		Object obj=null;
		Object ret=null;
		String methodName=null;
		obj=applicationContext.getBean(webAnnotationBean.getName());
		if(obj==null) return;
		MethodMetadata methodMetadata=webAnnotationBean.getMethodMetadata();
		Annotation[] methodAnnotations=null;
		if(methodMetadata!=null){
			methodAnnotations=methodMetadata.getMethodAnnotations();
			methodName=methodMetadata.getMethodName();
		}
		// 默认调用handleRequest方法
		if ("".equals(methodName)||methodName==null)
			methodName = DEFAULT_REQUEST_MAPPING;
		Representation representation=new Representation(request,response);
		representation.setServletContext(servletContext);
		representation.setApplicationContext(applicationContext);
		RequestParameter requestParameter=new RequestParameter(representation);
		Object[] parameterValues=requestParameter.getParameterValues(webAnnotationBean);
		if(parameterValues!=null&&parameterValues.length>0){
			ret=JBOSClassCaller.call(obj,methodName,parameterValues);
		}else{
			ret=JBOSClassCaller.call(obj,methodName);
		}
		//处理响应
		if(methodAnnotations!=null&&methodAnnotations.length>0){
			ResponseBody responseBody=null;
			for(Annotation annotation:methodAnnotations){
				if(annotation instanceof ResponseBody){
					responseBody=(ResponseBody)annotation;
					break;
				}
			}
			if(responseBody!=null){
				dispatcher.doDispatch(requestUri,Dispatcher.APPLICATION_JSON,ret);
			}else{
				dispatcher.doDispatch(requestUri,Dispatcher.APPLICATION_TEXT_HTML,ret);
			}
		}
	}

	/**
	 * 得到请求控制器对象
	 * @param applicationContext
	 * @param s
	 * @return
	 */
	protected Object getRequestController(ApplicationContext applicationContext,String s){	
		Object obj=null;
		if(s==null) return null;
		//obj=applicationContext.getBeanDefinition(s);
		if(obj==null){
			if(s.indexOf(".form")!=-1){		
				if(s.startsWith("/")){
					s=s.substring(1, s.indexOf(".form"));
				}else{
					s=s.substring(0, s.indexOf(".form"));
				}				
			}
			//obj=applicationContext.getBeanDefinition(s);
			//obj=obj!=null?obj:applicationContext.getBeanDefinition("/"+s);
		}				
		return obj;
	}
}
