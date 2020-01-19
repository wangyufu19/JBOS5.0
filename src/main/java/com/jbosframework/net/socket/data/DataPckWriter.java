package com.jbosframework.net.socket.data;
import java.io.IOException;
/**
 * 数据包写入
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public abstract class DataPckWriter {
	/**
	 * 构造方法
	 */
    public DataPckWriter(){
    	
    }
    /**
     * 写入字节
     * @param byteArray
     * @return
     * @throws IOException
     */
    public abstract int write(byte[] byteArray) throws IOException;	
   
}
