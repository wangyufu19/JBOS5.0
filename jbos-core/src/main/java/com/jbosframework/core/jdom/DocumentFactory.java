package com.jbosframework.core.jdom;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
/**
 * DocumentFactory
 * @author youfu.wang
 * @version 1.0
 */
public interface DocumentFactory {
	/**
	 * 得到文档对象
	 * @param location
	 * @return
	 */
	public Object getDocument(String location);
	/**
	 * 得到文档对象
	 * @param file
	 * @return
	 */
	public Object getDocument(File file);
	/**
	 * 得到文档对象
	 * @param is
	 * @return
	 */
	public Object getDocument(InputStream is);
	/**
	 * 得到文档对象
	 * @param url
	 * @return
	 */
	public Object getDocument(URL url);
	/**
	 * 得到文档对象
	 * @param uri
	 * @return
	 */
	public Object getDocument(URI uri);

}
