package com.jbosframework.net.socket.ext;
/**
 * 事件回调
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public interface EventCallback {
	/**
	 * 处理回调
	 * @param data
	 */
	public void handle(byte[] data);
	/**
	 * 得到结果
	 * @return
	 */
	public byte[] returnResult();
}
