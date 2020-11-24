package com.jbosframework.core;
import java.io.StringReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
/**
 * JDOMBuilder
 * @author youfu.wang
 * @version 1.0
 */
public class JDOMBuilder {
	private static JDOMBuilder instance=null;
	/**
	 * 得到JDOMBuilder实例
	 * @return
	 */
	public static JDOMBuilder getJDOMBuilder(){
		if(instance==null){
			instance=new JDOMBuilder();
		}
		return instance;
	}
	/**
	 * 解析xml字符串
	 * @param xml
	 * @return
	 */
	public Document parseXml(String xml){
		StringReader io=new StringReader(xml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc=builder.build(io);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	/**
	 * 解析xml文件
	 * @param resource
	 * @return
	 */
	public Document parse(String resource) {
		return this.parse(new File(resource));
	}
	/**
	 * 解析xml文件
	 * @param file
	 * @return
	 */
	public Document parse(File file) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(file);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	/**
	 * 解析xml输入流
	 * @param stream
	 * @return
	 */
	public Document parse(InputStream stream) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(stream);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	/**
	 * 解析xml的Url
	 * @param url
	 * @return
	 */
	public Document parse(URL url) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(url);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
}
