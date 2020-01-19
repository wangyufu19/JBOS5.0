package com.jbosframework.net.socket.data;
import java.io.IOException;
/**
 * 数据包读取
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public abstract class DataPckReader {
	/**
	 * 构造方法
	 */
    public DataPckReader(){
    	
    }
    /**
     * 读取字节
     * @return
     * @throws IOException
     */
	public abstract byte[] read() throws IOException;
	
}
