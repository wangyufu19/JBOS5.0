package com.jbosframework.web.mvc.data;
/**
 * Charset
 * @author youfu.wang
 * @version 1.0
 */
public class Charset {
	public static final String UTF8="utf-8";
	public static final String GBK="gbk";
	public static final String GB2312="gb2312";	
	private String charset="utf-8";
	/**
	 * 构造方法
	 */
	public Charset(){
		this.charset=UTF8;
	}
	/**
	 * 构造方法
	 * @param charset
	 */
	public Charset(String charset){
		this.charset=charset;
	}
	/**
	 * 得到字符集
	 * @return
	 */
	public String getCharset() {
		return charset;
	}
	/**
	 * 设置字符集
	 * @param charset
	 */
	public void setCharset(String charset) {
		if("".equals(charset)||charset==null||"null".equals(charset)){
			this.charset=UTF8;
		}
		if(GBK.equals(charset)){
			this.charset=GBK;
		}else if(GB2312.equals(charset)){
			this.charset=GB2312;
		}else if(UTF8.equals(charset)){
			this.charset=UTF8;
		}		
	}
}
