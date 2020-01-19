package com.jbosframework.net.socket;
import java.io.IOException;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.UnknownHostException;
import com.jbosframework.net.socket.data.DataPackage;
import com.jbosframework.net.socket.ext.EndpointExt;
/**
 * Socket客户端
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public class SocketClient {
	/**
	 * 终端对象实例
	 */
	private Endpoint endpoint;
	/**
	 * 连接状态
	 */
	private boolean connectionStatus=false;
	/**
	 * Socket对象实例
	 */
	private Socket socket;
	/**
	 * 构造方法
	 * @param endpoint
	 */
	public SocketClient(Endpoint endpoint){
		this.endpoint=endpoint;
	}
	/**
	 * 连接远程终端
	 */
	public void connect(){		 
		if(!connectionStatus){
			String host=this.endpoint.getHost();
	    	int port=this.endpoint.getPort();
			try {
				socket=new Socket(host,port);
				connectionStatus=true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				connectionStatus=false;
			} catch (IOException e) {				
				e.printStackTrace();
				connectionStatus=false;
			}
		}
	}
	/**
	 * 数据交换
	 * @param data
	 */
	public void exchange(String data){	
		System.out.println("******connectionStatus: "+connectionStatus);
		if(connectionStatus==false){
			System.out.println("*****连接服务器["+this.endpoint.getHost()+"]失败");
			return;
		}
		
		DataOutputStream out=null;
		DataInputStream in=null;
		try {
			DataPackage dataPackage=new DataPackage();
			dataPackage.setContent(data);
			out=new DataOutputStream(socket.getOutputStream());	
			out.write(dataPackage.getToStringToByte());
			out.flush();			
			in=new DataInputStream(socket.getInputStream());
			byte[] buffer=new byte[1024];
			int readBytes=0;
			readBytes=in.read(buffer);
			System.out.println(new String(buffer,Endpoint.CHARSET_GB2312));
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {		
			e.printStackTrace();
		}		
	}
	/**
	 * 关闭Socket
	 */
	public void close(){
		if(connectionStatus){
			try {
				socket.close();
				connectionStatus=false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException{
	
		for(int i=0;i<100;i++){
			Endpoint endpoint=new EndpointExt("127.0.0.1",9080);
			SocketClient socketClient=new SocketClient(endpoint);
			socketClient.connect();
			socketClient.exchange("004|3|6901028001465");
			Thread.sleep(200);
		}
		
	}
}
