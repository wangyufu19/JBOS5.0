package com.jbosframework.net.socket.ext;
import com.jbosframework.net.socket.Endpoint;

/**
 * 通道终端类
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public class EndpointExt implements Endpoint{
	/**
	 * 主机地址
	 */
	private String host;
	/**
	 * 端口号
	 */
	private int port=8090;
	/**
	 * 字符集
	 */
	private String charset;
	
	/**
	 * 构造方法
	 */
	public EndpointExt(){
		this.charset=Endpoint.CHARSET_UTF8;
	}
	/**
	 * 构造方法
	 * @param port
	 */
    public EndpointExt(int port){
    	this.charset=Endpoint.CHARSET_UTF8;
    	this.port=port;
	}
    /**
     * 构造方法
     * @param host
     * @param port
     */
	public EndpointExt(String host,int port){
		this.host=host;
		this.port=port;
		this.charset=Endpoint.CHARSET_UTF8;
	}
	/**
	 * 设置字符集
	 */
	public void setCharset(String charset){
		this.charset=charset;
	}
	/**
	 * 得到字符集
	 */
	public String getCharset(){
		return this.charset;
	}
	/**
	 * 得到主机
	 */
	public String getHost() {
		return this.host;
	}
	/**
	 * 得到端口号
	 */
	public int getPort() {
		return this.port;
	}	
	
}
