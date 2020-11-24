package com.jbosframework.net.socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jbosframework.net.socket.data.DataPackage;
import com.jbosframework.net.socket.exchange.DataCallback;
import com.jbosframework.net.socket.exchange.DataCallbackExt;
import com.jbosframework.net.socket.exchange.IDataExchange;
import com.jbosframework.net.socket.exchange.TcpDataExchange;
import com.jbosframework.net.socket.ext.EndpointExt;
/**
 * SocketServerService
 * @author youfu.wang
 * @date 2016-07-14
 */
public class SocketServerService extends Thread{
	protected Log logger=LogFactory.getLog(this.getClass());
	private ServerSocket serverSocket=null;
	private Endpoint endpoint=null;
	private IDataExchange iDataExchange=null;
	private DataCallback dataCallback=null;
	
	public SocketServerService(Endpoint endpoint){
		this.endpoint=endpoint;
	}
	public void setEndpoint(Endpoint endpoint){
		this.endpoint=endpoint;
	}
	public void setIDataExchange(IDataExchange iDataExchange){
		this.iDataExchange=iDataExchange;
	}
	public void setDataCallback(DataCallback dataCallback){
		this.dataCallback=dataCallback;
	}
	
	public void run(){	
		try {
			serverSocket = new ServerSocket(this.endpoint.getPort());
			while(!Thread.interrupted()){				
				Socket socket=serverSocket.accept();
				logger.info("******accept connect");
				Acceptor acceptor=new Acceptor(socket);	
				acceptor.start();
			}
		} catch (IOException e) {
			e.printStackTrace();			
		}		
	}

	public class Acceptor extends Thread{
		private Socket socket;
		
		public Acceptor(){
			
		}
		public Acceptor(Socket socket){
			this.socket=socket;
		}		
		public void run(){
			try {
				this.handle();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		private void handle() throws IOException{
			DataOutputStream out=null;
			DataInputStream in=null;
			if(iDataExchange!=null){
				in=new DataInputStream(socket.getInputStream());
				byte[] readBytes=null;
				logger.info("******读取客户端数据");
				readBytes=iDataExchange.read(in);
				if(readBytes!=null){
					if(readBytes.length>0){
						String[] retDatas=dataCallback.handle(readBytes);					
						if(retDatas!=null){
							logger.info("******响应数据给客户端");
							for(int i=0;i<retDatas.length;i++){
								out=new DataOutputStream(socket.getOutputStream());	
								DataPackage dataPackage = new DataPackage();
								dataPackage.setContent(retDatas[i]);
								try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								iDataExchange.write(out,dataPackage.getToStringToByte(endpoint.getCharset()));					
							}
							if(out!=null){
								out.flush();
								out.close();
							}						
						}	
					}
				}
			}
			if(in!=null){
				in.close();		
			}			
			socket.close();
		}
	}
	
	public static void main(String[] args){
		Endpoint endpoint=new EndpointExt(8090);
		endpoint.setCharset(Endpoint.CHARSET_GB2312);
		SocketServerService socketServerService=new SocketServerService(endpoint);
		IDataExchange iDataExchange=new TcpDataExchange();
		DataCallback dataCallback=new DataCallbackExt();
		socketServerService.setIDataExchange(iDataExchange);
		socketServerService.setDataCallback(dataCallback);
		socketServerService.start();
	}
}
