package com.jbosframework.net.socket.data;
import java.util.Map;
import com.jbosframework.net.socket.Endpoint;
import com.jbosframework.net.socket.data.DataPackage;
import com.jbosframework.net.socket.data.IDataPackage;
/**
 * XML数据包
 * @author youfu.wang
 * @date 2016-04-23
 */
public class XmlDataPackage implements IDataPackage{
	/**
	 * 字符集
	 */
	private String charset=Endpoint.CHARSET_UTF8;
	/**
	 * 得到字符集
	 */
	public String getCharset() {
		return charset;
	}
	/**
	 * 设置字符集
	 */
	public void setCharset(String charset) {
		this.charset=charset;
	}
	/**
	 * 打包数据
	 */
	public byte[] pack() {	
		return null;
	}
	/**
	 * 打包数据
	 */
	public byte[] pack(String data) {
		byte[] bytes=null;
		DataPackage dataPackage=new DataPackage();
		dataPackage.setContent(data);
		bytes=dataPackage.getToStringToByte();
		return bytes;
	}
	/**
	 * 打包数据
	 */
	public byte[] pack(Map<String, Object> data) {		
		return null;
	}
	/**
	 * 解包数据
	 */
	public void unpack(byte[] byteArray){
		
	}
	
}
