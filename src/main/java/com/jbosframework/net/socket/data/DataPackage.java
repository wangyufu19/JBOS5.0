package com.jbosframework.net.socket.data;
import java.io.UnsupportedEncodingException;
/**
 * 数据包类
 * @author youfu.wang
 * @version 1.0
 * @date 2015-04-25
 */
public class DataPackage {
	public static final String CHARSET_UTF8="UTF-8";
	public static final String CHARSET_GBK="GBK";
	public static final String CHARSET_GB2312="GB2312";	
	/**
	 * 数据包最大值
	 */
	public static final int PCK_MAX_VALUE=4096;
	/**
	 * 数据包最小值
	 */
	public static final int PCK_MIN_VALUE=2048;
	/**
	 * 包头
	 */
	private String head;
	/**
	 * 长度
	 */
	private int length=0;	
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 后缀
	 */
	private String suffix;	
	/**
	 * 数据包
	 */
	private String toString;
	/**
	 * 得到包头
	 * @return
	 */
	public String getHead() {
		if(head==null||"null".equals(head)){
			return "";
		}
		return head;
	}
	/**
	 * 设置包头
	 * @param head
	 */
	public void setHead(String head) {
		this.head = head;
	}
	/**
	 * 得到包长度
	 * @return
	 */
	public int getLength() {
		return length;
	}
	/**
	 * 得到包长度8位字符串格式[0000000]
	 * @return
	 */
	public String getLengthToString(){
		String lenStr="";
		if(length<10){
			lenStr="0000000"+length;
		}else if(length<100){
			lenStr="000000"+length;
		}else if(length<1000){
			lenStr="00000"+length;
		}else if(length<10000){
			lenStr="0000"+length;
		}		
		return lenStr;
	}
	/**
	 * 设置包长度
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}
	/**
	 * 得到内容
	 * @return
	 */
	public String getContent() {
		if(content==null||"null".equals(content)){
			return "";
		}
		return content;
	}
	/**
	 * 设置内容
	 * @param content
	 * @param charset
	 */
	public void setContent(String content) {
		this.content = content;
		if(this.content!=null){
			try {
				this.length=content.getBytes(DataPackage.CHARSET_UTF8).length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 设置内容
	 * @param content
	 * @param charset
	 */
	public void setContent(String content,String charset) {
		this.content = content;
		if(this.content!=null){
			try {
				this.length=content.getBytes(charset).length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 得到后缀
	 * @return
	 */
	public String getSuffix() {
		if(suffix==null||"null".equals(suffix)){
			return "";
		}
		return suffix;
	}
	/**
	 * 设置后缀
	 * @param suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	/**
	 * 得到数据包
	 * @return
	 */
	public String getToString() {
		return toString;
	}
	/**
	 * 得到数据包字节
	 * @return
	 */
	public byte[] getToStringToByte(String charset){
		byte[] bytes=null;
		try {
			bytes=(this.getHead()+this.getLengthToString()+this.getContent()+this.getSuffix()).getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	/**
	 * 得到数据包字节
	 * @return
	 */
	public byte[] getToStringToByte(){
		byte[] bytes=null;
		try {
			bytes=(this.getHead()+this.getLengthToString()+this.getContent()+this.getSuffix()).getBytes(DataPackage.CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	/**
	 * 设置数据包
	 * @param toString
	 */
	public void setToString(String toString) {
		this.toString = toString;
	}
}
