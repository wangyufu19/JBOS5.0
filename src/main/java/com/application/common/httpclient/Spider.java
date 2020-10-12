package com.application.common.httpclient;
import java.lang.Thread;
import java.util.Map;
import java.util.HashMap;

/**
 * Spider
 * @author youfu.wang
 * @version 1.0
 */
public class Spider extends Thread{	
	private String requestMethod=HttpClientHandler.REQUEST_METHOD_POST;	
	private boolean multipart=false;
	private Map<String,Object> requestParameter=new HashMap<String,Object>();
	private HttpClientHandler httpClientHandler=null;
	/**
	 * 构造方法
	 * @param url
	 * @param method
	 */
	public Spider(String url,String method){
		httpClientHandler=new HttpClientHandler(url);
		this.requestMethod=method;
	}
	/**
	 * 设置multipart
	 * @param multipart
	 */
	public void setMultipart(boolean multipart){
		this.multipart=multipart;
	}
	/**
	 * 设置请求参数
	 * @param requestParameter
	 */
	public void setRequestParameter(Map<String,Object> requestParameter){
		this.requestParameter=requestParameter;
	}
	/**
	 * 设置请求参数
	 * @param name
	 * @param value
	 */
	public void setRequestParameter(String name,Object value){
		requestParameter.put(name,value);
	}
	@Override
	public void run() {
		if(requestMethod.equals(HttpClientHandler.REQUEST_METHOD_POST)){
			if(multipart){
				httpClientHandler.doPost(requestParameter,this.multipart);	
			}else{
				try {
					httpClientHandler.doPost(requestParameter);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			
		}else if(requestMethod.equals(HttpClientHandler.REQUEST_METHOD_GET)){
			httpClientHandler.doGet();
		}
		httpClientHandler.close();
	}
	
	public static void main(String[] args){
		String url="http://127.0.0.1:8090/demo/getUserInfo";
		for(int i=0;i<10000;i++) {
			Spider spider=new Spider(url,HttpClientHandler.REQUEST_METHOD_GET);
			spider.start();
		}
		
	}
}
