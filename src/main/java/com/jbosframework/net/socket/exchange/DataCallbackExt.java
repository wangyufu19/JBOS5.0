package com.jbosframework.net.socket.exchange;
import java.io.UnsupportedEncodingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jbosframework.net.socket.Endpoint;
/**
 * 事件回调实现
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public class DataCallbackExt implements DataCallback{
	private static Log log=LogFactory.getLog(DataCallbackExt.class);
	/**
	 * 数据内容分隔符
	 */
	public static final char PAD_PACKAGE_SEP='|';
	
	public String[] handle(byte[] data){
		try {
			System.out.println("******接收报文: "+new String(data,Endpoint.CHARSET_UTF8));
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		String[] ret=new String[2];
		ret[0]="Hello World!";
		ret[1]="NingBo!";
		return ret;
	}
}
