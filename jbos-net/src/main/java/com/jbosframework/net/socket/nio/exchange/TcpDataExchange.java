package com.jbosframework.net.socket.nio.exchange;
import java.io.IOException;
import java.nio.channels.SocketChannel;
/**
 * TCP数据交换接口
 * @author youfu.wang
 * @date 2016-04-23
 */
public class TcpDataExchange implements IDataExchange{
	private SocketChannel socketChannel=null;
	/**
	 * 构造方法
	 */
	public TcpDataExchange(){

	}
	/**
	 * 构造方法
	 * @param socketChannel
	 */
	public TcpDataExchange(SocketChannel socketChannel){
		this.socketChannel=socketChannel;
	}
	/**
	 * 设置SocketChannel
	 * @param socketChannel
	 */
	public void setSocketChannel(SocketChannel socketChannel){
		this.socketChannel=socketChannel;
	}
	/**
	 * 通过java自带函数判断字符串是否数字
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str){
		if(str==null||"".equals(str)) return false;
		for (int i = str.length();--i>=0;){   			
		    if (!Character.isDigit(str.charAt(i))){
		    	return false;
		    }
		}
	    return true;
	}
	/**
	 * 写数据包
	 * @param dataPackage
	 * @param charset
	 * @throws IOException 
	 */
	public int write(byte[] byteArray) throws IOException{
		int writeBytes=0;
		if(byteArray==null)
			return writeBytes;		
		ChannelBuffer channelBuffer=new ChannelBuffer(socketChannel);
		writeBytes=channelBuffer.write(byteArray);		
		return writeBytes;
	}
	/**
	 * 读数据包
	 * @return
	 * @throws IOException 
	 */
	public byte[] read() throws IOException{				
		byte[] prefix=null;
		byte[] readBytes=null;
		ChannelBuffer channelBuffer=new ChannelBuffer(socketChannel);
		//读取报文前缀
		prefix=channelBuffer.read(8);	
		String prefixStr=new String(prefix);
		System.out.println("******prefixStr: "+prefixStr);
		//判断报文前缀是否8位数字
		if(isNumeric(prefixStr)){
			//读取报文内容
			readBytes=channelBuffer.read(Integer.parseInt(prefixStr));
		}
		return readBytes;
	}
}
