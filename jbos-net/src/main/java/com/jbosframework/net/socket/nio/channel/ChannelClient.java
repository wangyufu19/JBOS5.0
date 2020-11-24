package com.jbosframework.net.socket.nio.channel;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import com.jbosframework.net.socket.Endpoint;
import com.jbosframework.net.socket.data.IDataPackage;
import com.jbosframework.net.socket.data.XmlDataPackage;
import com.jbosframework.net.socket.ext.EndpointExt;
import com.jbosframework.net.socket.nio.exchange.IDataExchange;
import com.jbosframework.net.socket.nio.exchange.TcpDataExchange;
/**
 * 通道客户端
 * @author youfu.wang
 * @version 1.0
 * @create-date 2015-04-25
 */
public class ChannelClient{
	private Endpoint endpoint;
	private SocketChannel socketChannel=null;
	private int connectionStatus=Endpoint.CHANNEL_NOCONNECTED;
	
	public ChannelClient(Endpoint endpoint){
		this.endpoint=endpoint;
	}
	/**
	 * 连接服务端
	 */
	public void connect(){		 
		if(connectionStatus!=Endpoint.CHANNEL_CONNECTED){
			try { 
				String host=this.endpoint.getHost();
		    	int port=this.endpoint.getPort();
		    	InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
		    	socketChannel= SocketChannel.open(inetSocketAddress);             
		        socketChannel.configureBlocking(false);  
		        connectionStatus=Endpoint.CHANNEL_CONNECTED;
			} catch (IOException e) {
				e.printStackTrace();
				socketChannel=null;
				if(e.getMessage().indexOf("Connection timed out: connect")!=-1){
					connectionStatus=Endpoint.CHANNEL_CONNECTIION_TIMEOUT;
				}else if(e.getMessage().indexOf("Connection refused: connect")!=-1){
					connectionStatus=Endpoint.CHANNEL_CONNECTIION_FAILURE;
				}else if(e.getMessage().indexOf("java.nio.channels.UnresolvedAddressException")!=-1){
					connectionStatus=Endpoint.CHANNEL_CONNECTIION_NOVALID_ADDRESS;
				}else{
					connectionStatus=Endpoint.CHANNEL_CONNECTIION_FAILURE;
				}
			} 
		}		
	}
	/**
	 * 得到SocketChannel
	 * @return
	 */
	public SocketChannel getSocketChannel(){
		return this.socketChannel;
	}
	/**
	 * 得到连接状态
	 * @return
	 */
	public int getConnectionStatus(){
		return this.connectionStatus;
	} 
	/**
	 * 得到终端
	 * @return
	 */
	public Endpoint getEndpoint(){
		return this.endpoint;
	}
	/**
	 * 关闭终端
	 */
    public void close(){
    	if(connectionStatus==Endpoint.CHANNEL_CONNECTED){
    		try {	    			
				socketChannel.close();				
				socketChannel=null;
				connectionStatus=Endpoint.CHANNEL_NOCONNECTED;
			} catch (IOException e) {
				e.printStackTrace();
				socketChannel=null;		
				connectionStatus=Endpoint.CHANNEL_CONNECTIION_OTHER;
			}
    	}
    }
    public static void main(String[] args) throws InterruptedException{
    	final String writeStr="hello world!";
    	long startTime=System.currentTimeMillis();
		for(int i=0;i<1;i++){
			new Thread(){
				public void run(){
					Endpoint endpoint=new EndpointExt("127.0.0.1",8090);
		        	endpoint.setCharset(Endpoint.CHARSET_UTF8);
		        	ChannelClient channelClient=new ChannelClient(endpoint);
		        	channelClient.connect();        	
//		        	ChannelBuffer channelBuffer=new ChannelBuffer(channelClient.getSocketChannel());
		        	IDataExchange iDataExchange=new TcpDataExchange(channelClient.getSocketChannel());
		        	IDataPackage idataPackage=new XmlDataPackage();
		        	int writeBytes=0;    	        	
		        
					try {
						writeBytes=iDataExchange.write(idataPackage.pack(writeStr));
			        	if(writeBytes>0){
			        		byte[] readWrites;
			        		readWrites = iDataExchange.read();
			        		if(readWrites!=null){
			        			try {
									System.out.println(new String(readWrites,Endpoint.CHARSET_UTF8));
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
			        		}
							
			        	}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        		
		        	    	        	
		        	channelClient.close();
				}
			}.start();    		
			Thread.sleep(10);
		} 
		long endTime=System.currentTimeMillis();
		System.out.println("*******executeTimes: "+(endTime-startTime)/1000);
    }

}
