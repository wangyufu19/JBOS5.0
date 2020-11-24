package com.jbosframework.net.socket;

/**
 * 终端接口
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public interface Endpoint {
	public static final String CHARSET_UTF8="UTF-8";
	public static final String CHARSET_GBK="GBK";
	public static final String CHARSET_GB2312="GB2312";	

	/**
	 * 未连接
	 */
	public static final int CHANNEL_NOCONNECTED=0;
	/**
	 * 已连接
	 */
	public static final int CHANNEL_CONNECTED=1;
	/**
	 * 连接失败
	 */
	public static final int CHANNEL_CONNECTIION_FAILURE=2;
	/**
	 * 连接关闭
	 */
	public static final int CHANNEL_CONNECTIION_CLOSED=3;
	/**
	 * 连接超时
	 */
	public static final int CHANNEL_CONNECTIION_TIMEOUT=4;	
	/**
	 * 无效地址
	 */
	public static final int CHANNEL_CONNECTIION_NOVALID_ADDRESS=5;
	/**
	 * 其它异常
	 */
	public static final int CHANNEL_CONNECTIION_OTHER=10;
	/**
	 * 设置字符集
	 * @param charset
	 */
	public void setCharset(String charset);	
	/**
	 * 得到字符集
	 * @return
	 */
	public String getCharset();
	/**
	 * 得到主机
	 * @return
	 */
	public String getHost();
	/**
	 * 得到商品号
	 * @return
	 */
	public int getPort();

}
