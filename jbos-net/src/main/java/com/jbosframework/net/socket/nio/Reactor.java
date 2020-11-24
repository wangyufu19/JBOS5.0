package com.jbosframework.net.socket.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import com.jbosframework.net.socket.ext.EventCallback;
import com.jbosframework.net.socket.ext.EventCallbackExt;
import com.jbosframework.net.socket.nio.exchange.IDataExchange;
import com.jbosframework.net.socket.nio.exchange.TcpDataExchange;

/**
 * 反应器模式 用于解决多用户访问并发问题
 */
public class Reactor implements Runnable {
    public final Selector selector;
    public final ServerSocketChannel serverSocketChannel;
    /**
	 * 数据交换接口
	 */
	private IDataExchange idataExchange=null;
	/**
	 * 事件回调接口
	 */
	private EventCallback eventCallback=null;

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
    public Reactor(int port) throws IOException {
    	serverSocketChannel=ServerSocketChannel.open();
		ServerSocket ss=serverSocketChannel.socket();
		InetSocketAddress inetAddrs=new InetSocketAddress(port);
		ss.bind(inetAddrs);
		serverSocketChannel.configureBlocking(false);
		selector=Selector.open();
		SelectionKey selectionKey=serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);	
		Acceptor acceptor=new Acceptor(this);
		selectionKey.attach(acceptor);
		System.out.println("******启动通监听端口:"+port);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey selectionKey = it.next();
                    dispatch(selectionKey);
                    selectionKeys.clear();
                }
            }
            System.out.println("======================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 运行Acceptor或SocketReadHandler
     * 
     * @param key
     */
    void dispatch(SelectionKey key) {
        Runnable r = (Runnable) (key.attachment());
        if (r != null) {
            r.run();
        }
    }
    public class Acceptor implements Runnable {
        private Reactor reactor;

        public Acceptor(Reactor reactor) {
            this.reactor = reactor;
        }

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = reactor.serverSocketChannel.accept();
                if (socketChannel != null){
                    // 调用Handler来处理channel
                    new SocketReadHandler(reactor.selector, socketChannel);
                }                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class SocketReadHandler implements Runnable {
        private SocketChannel socketChannel;
        private SelectionKey selectionKey;
        private static final int READING = 0, SENDING = 1;  
		int state = READING;  
		
        public SocketReadHandler(Selector selector, SocketChannel socketChannel) throws IOException {
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            selectionKey = socketChannel.register(selector, 0);
            selectionKey.attach(this);
            selectionKey.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        /**
         * 处理读取数据
         */
        @Override
        public void run() {            
        	if(idataExchange!=null){
				idataExchange.setSocketChannel(socketChannel);
			}
        	try {  
	            if (state == READING) {  
	                read();  
	            } else if (state == SENDING) {  
	                send();  
	            }  
	        } catch (IOException ex) {  
	            ex.printStackTrace();  
	        }  
        }
        private synchronized void read() throws IOException{
			if(selectionKey.readyOps()!=0&&selectionKey.isReadable()){
				//接收读取事件	
				byte[] readBytes=null;
				readBytes=idataExchange.read();	  
				if(readBytes!=null){
	            	if(readBytes.length>0){
	            		if(eventCallback!=null){
	            			//回调处理接收读取事件
	            			eventCallback.handle(readBytes);
	            			state = SENDING;  
            		        // Interested in writing  
            		        selectionKey.interestOps(SelectionKey.OP_WRITE); 
	            		}
	            	}
				 }
			}
		}
		public void send() throws IOException{
			//写入读取事件返回结果   
    		idataExchange.write(eventCallback.returnResult());
    		selectionKey.interestOps(SelectionKey.OP_READ);  
    	    state = READING;  
		}
    }
    public static void main(String[] args) throws IOException{
		Reactor reactor=new Reactor(8090);
		reactor.setIDataExchange(new TcpDataExchange());
		reactor.setEventCallback(new EventCallbackExt());
		Thread t=new Thread(reactor);
		t.start();
		System.out.println("======================");
	}
}


