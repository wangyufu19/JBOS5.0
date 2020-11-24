package com.jbosframework.message.httpclient;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

/**
 * HttpClientHandler
 * @author youfu.wang
 * @version 1.0
 */
public class HttpClientHandler {	
	public static final String REQUEST_METHOD_POST="POST";
	public static final String REQUEST_METHOD_GET="GET";	
	public static final String APPLICATION_JSON = "application/json";
	public static final String TEXT_PLAIN="text/plain";
	public static final String charset="UTF-8";
	private CloseableHttpClient httpClient = HttpClients.createDefault();
	private String url;
	private Map<String,String> headers=new HashMap<String,String>();
	private ResponseBody responseBody;
	/**
	 * 构造方法
	 * @param url
	 */
	public HttpClientHandler(String url){
		this.url=url;
	}
	/**
	 * 设置响应内容对象
	 * @param responseBody
	 */
	public void setResponseBody(ResponseBody responseBody){
		this.responseBody=responseBody;
	}
	/**
	 * 处理响应
	 * @param response
	 */
	private void handleHttpResponse(HttpResponse response) {    
        HttpEntity entity = response.getEntity();              
        String body = null;  
        try {  
            body = EntityUtils.toString(entity);             
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(responseBody!=null){
        	responseBody.handle(body);
        }		
    }

	/**
	 * 添加请求头值
	 * @param name
	 * @param value
	 */
    public void addHeader(String name,String value){
		headers.put(name,value);
	}
	/**
	 * 执行post请求
	 * @param data
	 */
	public void doPost(String data) {
		HttpPost httpPost = new HttpPost(url);
		for(Map.Entry<String,String> entry:headers.entrySet()){
			httpPost.setHeader(entry.getKey(),entry.getValue());
		}
		StringEntity se = null;
		try {
			se = new StringEntity(data,charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setEntity(se);
        HttpResponse httpResponse = null;
		try {
			httpResponse=httpClient.execute(httpPost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.handleHttpResponse(httpResponse);	
	}
	/**
	 * 执行post请求
	 * @param params
	 */
	public void doPost(Map<String, Object> params) throws Exception{	
		HttpPost httpPost=this.getPostForm(url, params);
		HttpResponse httpResponse = null;  		  
		httpResponse = httpClient.execute(httpPost);  
		this.handleHttpResponse(httpResponse);	  
	}
	/**
	 * 执行post请求
	 * @param params
	 * @param multipart
	 */
	public void doPost(Map<String, Object> params,boolean multipart){	
		HttpPost httpPost=null;
		if(multipart){
			httpPost=this.getPostMultipartform(url, params);
		}else{
			httpPost=this.getPostForm(url, params);
		}	
		HttpResponse httpResponse = null;  
		try {  
			httpResponse = httpClient.execute(httpPost);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		this.handleHttpResponse(httpResponse);	  
	}
	/**
	 * 执行get请求
	 */
	public void doGet(){	
		HttpGet httpGet = new HttpGet(url);  
		HttpResponse httpResponse = null;  
		try {  
			httpResponse = httpClient.execute(httpGet);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		this.handleHttpResponse(httpResponse);
	}
	/**
	 *  关闭httpClient
	 */
	public void close(){
		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 得到表单参数
	 * @param url
	 * @param params
	 * @return
	 */
	private HttpPost getPostForm(String url, Map<String, Object> params){
		HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
        Set<String> keySet = params.keySet();  
        for(String key : keySet) {  
            nvps.add(new BasicNameValuePair(key,String.valueOf(params.get(key))));  
        }  
        try {        
            httpost.setEntity(new UrlEncodedFormEntity(nvps,charset));
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }            
        return httpost;  
	}
	/**
	 * 得到multipart表单参数
	 * @param url
	 * @param params
	 * @return
	 */
	public HttpPost getPostMultipartform(String url, Map<String, Object> params){
		HttpPost httpost = new HttpPost(url);  
		MultipartEntityBuilder multipartEntityBuilder= MultipartEntityBuilder.create();
		multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		for(String key : params.keySet()) { 
			Object value=params.get(key);
			if(value instanceof String){
				 StringBody paramStr = new StringBody(String.valueOf(value),ContentType.create(HttpClientHandler.TEXT_PLAIN,charset));
				 multipartEntityBuilder.addPart(key, paramStr);
			}else if(value instanceof File){
				 FileBody file = new FileBody((File)value);  
				 multipartEntityBuilder.addPart(key, file);
			}
		}
		try {
			multipartEntityBuilder.setCharset(CharsetUtils.get(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpEntity entity =multipartEntityBuilder.build();
		httpost.setEntity(entity);
		return httpost;  
	}
}
