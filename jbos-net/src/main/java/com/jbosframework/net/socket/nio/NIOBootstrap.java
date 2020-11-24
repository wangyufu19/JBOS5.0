package com.jbosframework.net.socket.nio;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jbosframework.net.socket.Endpoint;
import com.jbosframework.net.socket.ext.EndpointExt;
/**
 * NIO引导服务
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public class NIOBootstrap {
	private static Log log=LogFactory.getLog(NIOBootstrap.class);

	/**
	 * 构造方法
	 */
	public NIOBootstrap(){
		
	}

	/**
	 * 启动SocketBootstrap
	 */
	public void start(){
		
	}
	/**
	 * 停止SocketBootstrap
	 */
	public void stop(){
		log.info("*****SocketBootstrap已经停止");
		
	}

	public static void main(String[] args){
		Endpoint endpoint=new EndpointExt(8090);		
		endpoint.setCharset(Endpoint.CHARSET_UTF8);
		
		NIOBootstrap socketBootstrap=new NIOBootstrap();

		socketBootstrap.start();
	}
}
