package com.jbosframework.net.socket.data;
import java.util.Map;
/**
 * 数据包接口
 * @author youfu.wang
 * @date 2016-04-23
 */
public interface IDataPackage {
	/**
	 * 得到字符集
	 * @return
	 */
	public String getCharset();
	/**
	 * 设置字符集
	 * @param charset
	 */
	public void setCharset(String charset);
	/**
	 * 打包
	 * @return
	 */
	public byte[] pack();
	/**
	 * 打包
	 * @param data
	 * @return
	 */
	public byte[] pack(String data);
	/**
	 * 打包
	 * @param data
	 * @return
	 */
	public byte[] pack(Map<String, Object> data);
	/**
	 * 解包
	 * @param byteArray
	 */
	public void unpack(byte[] byteArray);	

}
