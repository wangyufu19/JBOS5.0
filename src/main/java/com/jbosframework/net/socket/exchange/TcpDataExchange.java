package com.jbosframework.net.socket.exchange;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import com.jbosframework.net.socket.data.DataPackage;
import com.jbosframework.net.socket.exchange.IDataExchange;
/**
 * 数据交换接口实现类
 * @author youfu.wang
 * @version 1.0
 */
public class TcpDataExchange implements IDataExchange{
	
	@Override
	public int write(DataOutputStream out,byte[] byteArray) throws IOException {
		int writeBytes=0;
		if(out!=null){
			writeBytes=byteArray.length;	
			out.write(byteArray);
		}		
		return writeBytes;
	}
	/**
	 * 通过java自带函数判断字符串是否数字
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str){
		if(str==null||"".equals(str)) return false;
		for (int i = str.length();--i>=0;){   			
		    if (!Character.isDigit(str.charAt(i))){
		    	return false;
		    }
		}
	    return true;
	}
	public byte[] read(DataInputStream in,int capacity) throws IOException{
		byte[] readBytes=new byte[capacity];
		int destPos=0;
		//可读取字节长度
		int remainingLength=capacity;
		while(remainingLength>0){
			int readLength=0;
			if(remainingLength>DataPackage.PCK_MIN_VALUE){
				readLength=DataPackage.PCK_MIN_VALUE;
			}else{
				readLength=remainingLength;
			}
			byte[] buffer=new byte[readLength];
			readLength=in.read(buffer);
			if(readLength>0){
				System.arraycopy(buffer, 0, readBytes, destPos, readLength);
				destPos=destPos+readLength;
				remainingLength=remainingLength-readLength;
			}
			
		}
		return readBytes;
	}
	@Override
	public byte[] read(DataInputStream in) throws IOException {	
		byte[] prefix=null;
		byte[] content=null;
		//读取报文头
		prefix=this.read(in, 8);
		if(prefix==null){
			return null;
		}
		System.out.println("******prefix: "+new String(prefix,DataPackage.CHARSET_UTF8));
		String prefixStr=new String(prefix);
		if(this.isNumeric(prefixStr)){
			//读取报文内容
			content=this.read(in,Integer.parseInt(prefixStr));
		}		
		return content;
	}
}
