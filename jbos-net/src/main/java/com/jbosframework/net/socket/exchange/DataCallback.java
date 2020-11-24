package com.jbosframework.net.socket.exchange;
/**
 * 事件回调
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public interface DataCallback {
	/**
	 * 处理回调
	 * @param data
	 */
	public String[] handle(byte[] data);

}
