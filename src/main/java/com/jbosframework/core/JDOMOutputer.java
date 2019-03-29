package com.jbosframework.core;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * JDOMOutputer
 * @author youfu.wang
 * @version 1.0
 */
public class JDOMOutputer {
	private static final String charset_code="utf-8";
	private static JDOMOutputer instance=null;
	/**
	 * 得到JDOMOutputer实例
	 * @return
	 */
	public static JDOMOutputer getJDOMOutputer(){
		if(instance==null){
			instance=new JDOMOutputer();
		}
		return instance;
	}
	/**
	 * 输出xml内容到文件
	 * @param doc
	 * @param path
	 */
	public void output(Document doc,String path){
		this.output(doc, new File(path));
	}
	/**
	 * 输出xml内容到文件
	 * @param doc
	 * @param file
	 */
	public void output(Document doc,File file){
		XMLOutputter outputter = new XMLOutputter(getFormat());
		FileOutputStream out=null;		
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {			
			e1.printStackTrace();
		}
		try {			
			outputter.output(doc, out);
			out.flush();
			out.close();			
		} catch (Exception e) {			
			e.printStackTrace(System.out);
		}		
	}
	/**
	 * 得到xml内容格式
	 * @return
	 */
	private Format getFormat() {
		Format format = Format.getPrettyFormat();
		format.setEncoding(charset_code);
		format.setIndent("  ");
		format.setExpandEmptyElements(false);
		return format;
	}
}
