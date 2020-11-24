package com.jbosframework.net.socket.nio.channel;
import java.io.IOException;
import java.lang.Runnable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jbosframework.net.socket.Endpoint;
import com.jbosframework.net.socket.ext.EndpointExt;
import com.jbosframework.net.socket.ext.EventCallback;
import com.jbosframework.net.socket.ext.EventCallbackExt;
import com.jbosframework.net.socket.nio.exchange.IDataExchange;
import com.jbosframework.net.socket.nio.exchange.TcpDataExchange;
/**
 * ChannelSelectorListener
 * @author youfu.wang
 * @date 2017-03-01
 */
public class ChannelSelectorListener implements Runnable{
	private static final Log log=LogFactory.getLog(ChannelSelectorListener.class);
	private ServerSocketChannel serverSocketChannel=null;
	private Selector selector=null;
	/**
	 * 数据交换接口
	 */
	private IDataExchange idataExchange;
	/**
	 * 事件回调接口
	 */
	private EventCallback eventCallback=null;
	/**
	 * 终端对象
	 */
	private Endpoint endpoint=null;	
	
	/**
	 * 构造方法
	 * @param endpoint
	 */
	public ChannelSelectorListener(Endpoint endpoint){
		this.endpoint=endpoint;
	}
	
	/**
	 * 设置数据交换接口
	 * @param idataExchange
	 */
	public void setIDataExchange(IDataExchange idataExchange){
		this.idataExchange=idataExchange;
	}
	/**
	 * 设置事件回调接口
	 * @param callback
	 */
	public void setEventCallback(EventCallback eventCallback){
		this.eventCallback=eventCallback;
	}
	@Override
	public void run() {
		try {
			log.info("******启动通监听端口:"+this.endpoint.getPort());
			serverSocketChannel=ServerSocketChannel.open();
			ServerSocket ss=serverSocketChannel.socket();
			InetSocketAddress inetAddrs=new InetSocketAddress(this.endpoint.getPort());
			ss.bind(inetAddrs);
			serverSocketChannel.configureBlocking(false);
			selector=Selector.open();
			SelectionKey selectionKey=serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);	
			Acceptor acceptor=new Acceptor();
			selectionKey.attach(acceptor);			
			this.listen(selector);
		} catch (IOException e) {
			e.printStackTrace();
			log.info("******启动通道监听器失败");
		}	
	}
	/**
	 * 轮询监听Selector
	 * @param selector
	 */
	private void listen(Selector selector){
		while(!Thread.interrupted()){					
			try {				
				if(selector.select()>0){
					Set<SelectionKey> selectionKeys=selector.selectedKeys();	
					if(selectionKeys.isEmpty()){
						continue;
					}
					for(SelectionKey selectionKey:selectionKeys){
						Runnable r = (Runnable) (selectionKey.attachment()); 
						if (r != null)
							r.run();	
					}				
					//清除处理过的事件
					selectionKeys.clear();
				}				
			} catch (IOException e) {
				e.printStackTrace();		
			}	
			System.out.println("======================");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Acceptor
	 * @author youfu.wang
	 * @date 2017-03-01
	 */
	public class Acceptor implements Runnable{
		SocketChannel socketChannel=null;
		SelectionKey selectionKey=null;
		@Override
		public void run() {			
			try {
				socketChannel=serverSocketChannel.accept();
				if(socketChannel!=null){
					socketChannel.configureBlocking(false);
					selectionKey=socketChannel.register(selector, SelectionKey.OP_READ);
					selector.wakeup();
					EventHandler eventHandler=new EventHandler(socketChannel,selectionKey);
					eventHandler.setIDataExchange(idataExchange);
					eventHandler.setEventCallback(eventCallback);
					selectionKey.attach(eventHandler);
				}
				System.out.println("Connection Accepted by Reactor");  
			} catch (IOException e) { 
				e.printStackTrace();
				if(selectionKey!=null){
					selectionKey.cancel();
				}
				if(socketChannel!=null){
					try {
						socketChannel.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
			}			
		}	
		/**
		 * EventHandler
		 * @author youfu.wang
		 * @date 2017-03-01
		 */
		public class EventHandler implements Runnable{
			private final SocketChannel socketChannel;
			private SelectionKey selectionKey;
			/**
			 * 数据交换接口
			 */
			private IDataExchange idataExchange;
			/**
			 * 事件回调接口
			 */
			private EventCallback eventCallback=null;
			/**
			 * 构造方法
			 * @param socketChannel
			 * @param selectionKey
			 * @throws IOException 
			 */
			public EventHandler(SocketChannel socketChannel,SelectionKey selectionKey) throws IOException{
				this.socketChannel=socketChannel;
				this.selectionKey=selectionKey;
			}
			/**
			 * 设置数据交换接口
			 * @param idataExchange
			 */
			public void setIDataExchange(IDataExchange idataExchange){
				this.idataExchange=idataExchange;
			}
			/**
			 * 设置事件回调接口
			 * @param eventCallback
			 */
			public void setEventCallback(EventCallback eventCallback){
				this.eventCallback=eventCallback;
			}
			@Override
			public void run() {
				//处理事件
				try {
					this.handle(selectionKey);
				} catch (IOException e) {
					this.closeChannel();
				}
			}
			/**
			 * 处理事件
			 * @param selectionKey
			 * @throws IOException
			 */
			private void handle(SelectionKey selectionKey) throws IOException{
				if(selectionKey.isReadable()){
					//接收读取事件
					if(idataExchange!=null){
						idataExchange.setSocketChannel(socketChannel);
						byte[] readBytes=null;
						readBytes=idataExchange.read();
						if(readBytes!=null){
							if(readBytes.length>0){
								if(eventCallback!=null){
									//回调处理接收读取事件
									eventCallback.handle(readBytes);
									//写入读取事件返回结果
									idataExchange.write(eventCallback.returnResult());
								}
							}
						}
					}
					this.closeChannel();
				}
			}

			/**
			 * 关闭通道
			 */
			private void closeChannel(){
				try {
					System.out.println("******closeChannel");
					selectionKey.cancel();
					socketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args){
		Endpoint endpoint=new EndpointExt(8090);		
		endpoint.setCharset(Endpoint.CHARSET_UTF8);
		ChannelSelectorListener selectorListener=new ChannelSelectorListener(endpoint);
		selectorListener.setIDataExchange(new TcpDataExchange());
		selectorListener.setEventCallback(new EventCallbackExt());
		Thread t=new Thread(selectorListener);
		t.start();
		System.out.println("======================");
	}
}
