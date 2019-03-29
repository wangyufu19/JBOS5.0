package com.jbosframework.web.mvc.dispatcher;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import com.jbosframework.beans.access.Setter;
import com.jbosframework.beans.access.BeanFactory;
import com.jbosframework.beans.config.AnnotationBean;
import com.jbosframework.context.ApplicationContext;
import com.jbosframework.web.mvc.ModelAndView;
import com.jbosframework.web.mvc.data.Represention;
import com.jbosframework.web.mvc.dispatcher.Dispatcher;
import com.jbosframework.core.JBOSClassloader;
import com.jbosframework.core.utils.TypeConverter;
/**
 * DispatchHandler
 * @author youfu.wang
 * @version 1.0
 */
public class DispatchHandler {	
	public static final String DEFAULT_REQUEST_MAPPING="handleRequest";
	private ApplicationContext applicationContext;
	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;	
	/**
	 * 构造方法
	 * @param request
	 * @param response
	 */
	public DispatchHandler(HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
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
		Object ret=null;	
		String s = this.getRequestPath();		
		
		controller=this.getRequestController(applicationContext,s);		
		Dispatcher dispatcher=new Dispatcher(this.request,this.response);
		
		if(controller==null){			
			dispatcher.doDispatcher404(s);
			return;
		}				
		if(controller instanceof AnnotationBean){				
			//使用注解加载配置的IoC对象	
			AnnotationBean annotationBean=(AnnotationBean)controller;		
			String requestMethod=request.getMethod().toLowerCase();
			boolean bool=false;
			if(annotationBean.getRequestMethod()!=null){
				String[] beanRequestMethod=annotationBean.getRequestMethod();
				for(int i=0;i<beanRequestMethod.length;i++){
					if(requestMethod.equals(beanRequestMethod[i])){
						ret=this.doHandle(applicationContext,annotationBean);
						bool=true;
						break;
					}else{
						bool=false;
					}
				}				
			}
			if(!bool){
				dispatcher.doDispatcher404(s);
				return;
			}
		}		
		if(ret==null){
			return;
		}
		if(ret instanceof ModelAndView){			
			dispatcher.doDispatch(s,(ModelAndView)ret);
		}else if(ret instanceof String){
			dispatcher.doPrintWriter(String.valueOf(ret));
		}		
	}
	/**
	 * 做处理分发
	 * @param applicationContext
	 * @param annotationBean
	 * @return
	 * @throws Exception
	 */
	protected Object doHandle(ApplicationContext applicationContext,AnnotationBean annotationBean) throws Exception{
		Object ret=null;
		Object obj=null;
		String methodName=null;
		obj=applicationContext.getBean(annotationBean.getName());
		if(obj==null) return null;
		methodName=annotationBean.getClassMethod();
		// 默认调用handleRequest方法
		if ("".equals(methodName)||methodName==null)
			methodName = DEFAULT_REQUEST_MAPPING;	
		ret=this.doInvokeMethod(obj, methodName);
		return ret;
	}
	/**
	 * 做方法调用
	 * @param obj
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	private Object doInvokeMethod(Object obj,String methodName) throws Exception{
		Object ret=null;
		if(obj==null){
			return null;
		}
		Method[] methods=null;
		Method method=null;	
		try {
			methods=obj.getClass().getMethods();
			if(methods!=null){
				for(int i=0;i<methods.length;i++){
					method=methods[i];
					if(method.getName().equals(methodName)){
						break;
					}
				}
			}
			if(method!=null){
				Object[] args=this.getMehtodParameters(method);
				ret =method.invoke(obj,args);
			}else{
				throw new NoSuchMethodException();
			}			
		} catch (NoSuchMethodException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (InvocationTargetException e) {
			throw e;
		}		
		return ret;
	}
	/**
	 * 得到方法参数
	 * @param method
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private Object[] getMehtodParameters(Method method) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class[] parameterTypes=null;
		Object[] args=null;
		Class parameterClass=null;
		Object parameterObject=null;
		Field[] parameterClassFields=null;
		if(method!=null){
			parameterTypes=method.getParameterTypes();
			if(parameterTypes!=null){
				args=new Object[parameterTypes.length];
				for(int i=0;i<parameterTypes.length;i++){
					Represention represention=new Represention(request,response);			
					represention.setServletContext(servletContext);
					represention.setApplicationContext(applicationContext);
					//判断是否接口或基本数据类型
					if(parameterTypes[i].isInterface()||TypeConverter.isPrimitiveType(parameterTypes[i])){
						continue;
					}else if(parameterTypes[i].getName().equals(Represention.class.getName())){		
						args[i]=represention;
					}else{
						//设置POJO对象字段值
						try {
							parameterClass=JBOSClassloader.loadClass(parameterTypes[i].getName());
						} catch (ClassNotFoundException e) {
							throw e;
						}
						if(parameterClass!=null){
							parameterObject=parameterClass.newInstance();
							parameterClassFields=parameterClass.getDeclaredFields();
							for(Field field:parameterClassFields){
								if(field.getType().isInterface()){
									continue;
								}
								Setter setter=BeanFactory.getSetter(parameterTypes[i], field.getName());
								if(TypeConverter.isPrimitiveType(field.getType())){										
									setter.set(parameterObject,represention.getParameter(field.getName(), false));
								}							
							}
						}	
						args[i]=parameterObject;
					}
				}
			}
		}
		return args;
	}
	/**
	 * 得到请求控制器对象
	 * @param applicationContext
	 * @param s
	 * @return
	 */
	protected Object getRequestController(ApplicationContext applicationContext,String s){	
		Object obj=null;
		if(s==null) return obj;								
		obj=applicationContext.getBeanDefinition(s);			
		if(obj==null){
			if(s.indexOf(".form")!=-1){		
				if(s.startsWith("/")){
					s=s.substring(1, s.indexOf(".form"));
				}else{
					s=s.substring(0, s.indexOf(".form"));
				}				
			}
			obj=applicationContext.getBeanDefinition(s);				
			obj=obj!=null?obj:applicationContext.getBeanDefinition("/"+s);
		}				
		return obj;
	}
	/**
	 * 得到请求控制器器方法
	 * @param s
	 * @return
	 */
	protected String getRequestControllerMethod(String s){	
		String methodName="";
		if(s==null||"".equals(s)){
			//调用默认handleRequest方法
			methodName = DEFAULT_REQUEST_MAPPING;		
		}		
		if(s.indexOf(".form")!=-1){			
			if(s.lastIndexOf("/")!=-1){
				methodName=s.substring(s.lastIndexOf("/")+1, s.indexOf(".form"));
			}else{
				methodName=s.substring(0, s.indexOf(".form"));
			}
		}		
		return methodName;
	}
	/**
	 * 是否Multipart
	 * @param request
	 * @return
	 */
	protected boolean isMultipartRequest(HttpServletRequest request){
		String contentType = request.getContentType();
		if (contentType != null) {
			if (contentType.indexOf("multipart/form-data") != -1) {
				return true;
			}
		}	
		return false;
	}
	/**
	 * 得到请求路径
	 * @return
	 */
	protected String getRequestPath() {
		String s = null;
		s = (String) request
				.getAttribute("javax.servlet.include.path_info");
		if (s == null)
			s = request.getPathInfo();
		if (s != null && s.length() >= 0) {
			return s;
		}
		s = (String)request.getAttribute("javax.servlet.include.servlet_path");
		if (s == null)
			s = request.getServletPath();	
		return s;
	}	
}
