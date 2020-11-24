package com.jbosframework.net.socket.nio.exchange;
import java.io.IOException;
import java.nio.channels.SocketChannel;
/**
 * 数据交换接口
 * @author youfu.wang
 * @date 2016-04-23
 */
public interface IDataExchange {
	/**
	 * 设置SocketChannel
	 * @param socketChannel
	 */
	public void setSocketChannel(SocketChannel socketChannel);
	/**
	 * 写入字节
	 * @param byteArray
	 * @return
	 */
	public int write(byte[] byteArray) throws IOException;
	/**
	 * 读取字节
	 * @return
	 */
	public byte[] read() throws IOException;
}
