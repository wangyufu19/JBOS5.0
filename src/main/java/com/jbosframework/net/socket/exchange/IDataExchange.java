package com.jbosframework.net.socket.exchange;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
/**
 * 数据交换接口
 * @author youfu.wang
 * @date 2016-04-23
 */
public interface IDataExchange {
	/**
	 * 写入字节
	 * @param byteArray
	 * @return
	 */
	public int write(DataOutputStream out, byte[] byteArray) throws IOException;
	/**
	 * 读取字节
	 * @return
	 */
	public byte[] read(DataInputStream in) throws IOException;
}
