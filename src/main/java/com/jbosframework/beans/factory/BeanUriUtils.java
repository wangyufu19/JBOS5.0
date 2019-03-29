package com.jbosframework.beans.factory;
/**
 * BeanUriUtils
 * @author youfu.wang
 * @version 1.0
 */
public class BeanUriUtils {
	/**
	 * 得到Bean的Uri
	 * @param parentUri
	 * @param uri
	 * @return
	 */
	public static String getBeanUri(String parentUri,String uri){
		String retUri="";
		if("".equals(parentUri)){
			retUri=uri;
		}else{
			if(parentUri.endsWith("/")&&uri.startsWith("/")){
				retUri=parentUri.substring(0, parentUri.lastIndexOf("/"))+uri;
			}else if(parentUri.endsWith("/")&&!uri.startsWith("/")){
				retUri=parentUri+uri;
			}else if(!parentUri.endsWith("/")&&uri.startsWith("/")){
				retUri=parentUri+uri;
			}else if(!parentUri.endsWith("/")&&!uri.startsWith("/")){
				retUri=parentUri+"/"+uri;
			}	
		}
		return retUri;
	}
}
