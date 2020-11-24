package com.jbosframework.net.socket.nio.exchange;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import com.jbosframework.net.socket.data.DataPackage;
/**
 * 通道缓存类
 * @author youfu.wang
 * @date 2016-04-29
 */
public class ChannelBuffer {
	/**
	 * SocketChannel对象实例
	 */
	private SocketChannel socketChannel;
	/**
	 * 构造方法
	 * @param socketChannel
	 */
	public ChannelBuffer(SocketChannel socketChannel){
		this.socketChannel=socketChannel;
	}		
	/**
	 * 读取字节
	 * @return
	 * @throws IOException 
	 */
	public byte[] read() throws IOException {	
        int readBytes=0;           
    	ByteBuffer byteBuffer=null;	
        byteBuffer=ByteBuffer.allocate(DataPackage.PCK_MIN_VALUE);
    	byteBuffer.clear();    	    	
    	while(true){     
    		readBytes=socketChannel.read(byteBuffer);
			if(readBytes>0){
				break;
			}
    	}       
		return byteBuffer.array();
	}	
	/**
	 * 读取字节
	 * @param capacity
	 * @return
	 * @throws IOException 
	 */
	public byte[] read(int capacity) throws IOException {	
		byte[] totoalBytes=new byte[capacity];	
		int destPos=0;
		ByteBuffer byteBuffer=null;	
		//可读取字节长度
		int remainingLength=capacity;
		System.out.println("******capacity: "+capacity);
		int bufferLength=0;			
		if(remainingLength>DataPackage.PCK_MIN_VALUE){
			bufferLength=DataPackage.PCK_MIN_VALUE;
		}else{
			bufferLength=remainingLength;
		}
		byteBuffer=ByteBuffer.allocate(bufferLength);
    	byteBuffer.clear();   
		while(remainingLength>0){
			int readBytes=0;			
	    	readBytes=socketChannel.read(byteBuffer);
	    	if(readBytes>0){
	    		System.arraycopy(byteBuffer.array(), 0, totoalBytes, destPos, readBytes);
				destPos=destPos+readBytes;
				remainingLength=remainingLength-readBytes;
	    	}			
		}
		return totoalBytes;
	}	 
	/**
	 * 写入字节
	 * @param byteArray
	 * @return
	 * @throws IOException 
	 */
	public int write(byte[] byteArray) throws IOException{
		int writeBytes=0;	
		if(byteArray==null)
			return writeBytes;		
		ByteBuffer byteBuffer=ByteBuffer.wrap(byteArray);
		writeBytes=socketChannel.write(byteBuffer);	
		return writeBytes;
	}	
}
