package com.jbosframework.core.jdom;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import com.jbosframework.core.JDOMBuilder;
/**
 * JDOMDocumentFactory
 * @author youfu.wang
 * @version 1.0
 */
public class JDOMDocumentFactory implements DocumentFactory{

	private static JDOMDocumentFactory instance=null;
	/**
	 * 构造方法
	 */
	public JDOMDocumentFactory(){
		
	}
	/**
	 * 得到JDOMDocumentFactory对象实例
	 * @return
	 */
	public static JDOMDocumentFactory getInstance(){
		if(instance==null){
			instance=new JDOMDocumentFactory();
		}
		return instance;
	}
	/**
	 * 得到文档对象
	 * @param location
	 * @return
	 */
	public Object getDocument(String location) {
		return this.getDocument(new File(location));
	}
	/**
	 * 得到文档对象
	 * @param file
	 * @return
	 */
	public Object getDocument(File file) {
		Object obj=null;
		JDOMBuilder builder=JDOMBuilder.getJDOMBuilder();
		obj=builder.parse(file);
		return obj;
	}
	/**
	 * 得到文档对象
	 * @param is
	 * @return
	 */
	public Object getDocument(InputStream is) {
		Object obj=null;
		JDOMBuilder builder=JDOMBuilder.getJDOMBuilder();
		obj=builder.parse(is);	
		return obj;
	}
	/**
	 * 得到文档对象
	 * @param url
	 * @return
	 */
	public Object getDocument(URL url) {
		Object obj=null;
		JDOMBuilder builder=JDOMBuilder.getJDOMBuilder();
		obj=builder.parse(url);		
		return obj;
	}
	/**
	 * 得到文档对象
	 * @param uri
	 * @return
	 */
	public Object getDocument(URI uri) {
		Object obj=null;		
		JDOMBuilder builder=JDOMBuilder.getJDOMBuilder();
		try {
			obj=builder.parse(uri.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}				
		return obj;
	}
}
