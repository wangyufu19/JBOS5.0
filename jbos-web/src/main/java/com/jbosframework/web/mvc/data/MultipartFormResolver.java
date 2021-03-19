package com.jbosframework.web.mvc.data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 * MultipartFormResolver
 * @author youfu.wang
 * @version 5.0
 */
public class MultipartFormResolver {

	public static final String MULTIPART_FILE="MULTIPART_FILE";
	private String charset="utf-8";
	private int maxSize=500*1024*1024;	
	private String repository="";
	private String saveDir="temp";
	private String extensions="bmp,jpg,jpeg,gif,zip,rar";	
	private String capacity="500*1024*1024";
	
	public MultipartFormResolver(){
		this.saveDir="";		
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
		if(this.capacity!=null){
			this.maxSize=Integer.parseInt(this.capacity)*1024*1024;
		}
	}
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public String getSaveDir() {
		return saveDir;
	}
	public void setSaveDir(String saveDir) {		
		this.saveDir = saveDir;		
	}
	public Map<String,String> handleMulitpartForm(HttpServletRequest request){
		Map<String,String> parameterValues=new LinkedHashMap<String,String>();
		DiskFileItemFactory factory=new DiskFileItemFactory();
		factory.setSizeThreshold(maxSize);
		factory.setRepository(new File(repository));
		//创建上传文件对象
		ServletFileUpload upload=new ServletFileUpload(factory);
		//设置上传最大文件
		upload.setSizeMax(maxSize);
		//处理表单上传请求
		List items=null;
		try {
			upload.setHeaderEncoding(charset);
			items=upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}	
		if(items==null){
			return parameterValues;
		}

		for(Iterator it=items.iterator();it.hasNext();){
			FileItem item=(FileItem)it.next();
			if(item.isFormField()){
				String name=item.getFieldName();
				String value=item.getString();						
				parameterValues.put(name,value);			
			}else{
				String name=item.getName();	
				if(name==null||"".equals(name)||!isAllowExtensions(name)) continue;
				name=this.getTimestampFileName(name);
				upload(item,name);		
				parameterValues.put(MULTIPART_FILE,name);	
			}
		}
		return parameterValues;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	private String getTimestampFileName(String name){
		String fileName="";
		if(name.lastIndexOf(".")!=-1){
			fileName=String.valueOf(System.nanoTime())+name.substring(name.lastIndexOf("."), name.length());							
		}	
		return fileName;
	}
	private void upload(FileItem item,String fileName){			
		String tmpPath="";
		if(repository.indexOf("/")!=-1){
			tmpPath=repository+File.separator+fileName;	
		}else
			tmpPath=repository+File.separator+fileName;
		
		File multipartFile=new File(tmpPath);
		if(!multipartFile.exists()){
			multipartFile.mkdirs();
		}			
		multipartFile.delete();	
		//写入文件到存储路径	
		FileOutputStream fos=null;
		InputStream in=null;
		try {
			fos = new FileOutputStream(multipartFile);
			if(item.isInMemory()){//如果上传的文件在内存
				fos.write(item.get());
			}else{				
				in=item.getInputStream();
				byte[] buffer=new byte[1024];
				int len=0;
				while((len=in.read(buffer))>0){
					fos.write(buffer, 0, len);
				}									
			}		
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(in!=null){
					in.close();
				}
				fos.close();
				//fos.flush();	
			} catch (IOException e) {
				e.printStackTrace();
			}
						
		}
	}
	private boolean isAllowExtensions(String fileName){
		boolean bool=false;
		String[] types=extensions.split(",");
		for(String type:types){					
			if(fileName.toLowerCase().endsWith("."+type)){
				bool=true;
				break;
			}
		}		
		return bool;
	}
	public String getExtensions() {
		return extensions;
	}
	public void setExtensions(String extensions) {
		this.extensions = extensions;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}	
}
