package com.jbosframework.net.socket.nio;//package com.jbosframework.net.socket.nio;
//import java.io.UnsupportedEncodingException;
//
//import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
//import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
//import org.apache.jmeter.samplers.SampleResult;
//
//import com.jbosframework.net.socket.Endpoint;
//import com.jbosframework.net.socket.data.IDataPackage;
//import com.jbosframework.net.socket.data.XmlDataPackage;
//import com.jbosframework.net.socket.nio.channel.ChannelClient;
//import com.jbosframework.net.socket.nio.channel.ChannelEndpoint;
//import com.jbosframework.net.socket.nio.exchange.IDataExchange;
//import com.jbosframework.net.socket.nio.exchange.TcpDataExchange;
///**
// * JMeterClientTestCase
// * @author youfu.wang
// * @date 2017-03-01
// */
//public class JMeterClientTestCase extends AbstractJavaSamplerClient{
//
//	@Override
//	public SampleResult runTest(JavaSamplerContext context) {
//		String ip=context.getParameter("ip");
//		String port=context.getParameter("port");
//		String message=context.getParameter("message");
//		Endpoint endpoint=new ChannelEndpoint(ip,Integer.parseInt(port));
//    	endpoint.setCharset(Endpoint.CHARSET_UTF8);
//    	ChannelClient channelClient=new ChannelClient(endpoint);
//    	channelClient.connect();        	
//    	IDataExchange idataExchange=new TcpDataExchange(channelClient.getSocketChannel());
//    	IDataPackage idataPackage=new XmlDataPackage();
//    	int writeBytes=0;    	        	
//    	writeBytes=idataExchange.write(idataPackage.pack(message));
//    	if(writeBytes>0){
//    		byte[] readWrites=idataExchange.read();
//    		try {
//				System.out.println(new String(readWrites,Endpoint.CHARSET_UTF8));
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//    	}    	        	
//    	channelClient.close();
//		return null;
//	}
//
//}
