package com.jbosframework.utils;
import javax.servlet.ServletOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipException;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


/**
 * 解压缩文件类
 * @author youfu.wang
 * @version 1.0
 */
public class FileZipUtils {
	
	public void zip(ServletOutputStream sos,String arg1,boolean flag){			
		File file=new File(arg1);			
		try {			
			ZipOutputStream zos=new ZipOutputStream(sos);
			zos.setEncoding("GBK");
			this.write(zos, file, "",flag);
			zos.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
	public void zip(String arg1,String arg2,boolean flag){		
		File file=new File(arg1);			
		try {
			ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(arg2));
			zos.setEncoding("GBK");
			this.write(zos, file, "",flag);
			zos.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
	public void unZip(String srcfile,String destDir){
		destDir=destDir.endsWith(File.separator)?destDir:destDir+File.separator;
		ZipFile zipFile = null;
		try {			
			zipFile=new ZipFile(new File(srcfile));
		} catch (ZipException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		byte[] buf=new byte[2048];	
		Enumeration enumeration=zipFile.getEntries();			
		try{
			while(enumeration.hasMoreElements()){
				ZipEntry zipEntry=(ZipEntry)enumeration.nextElement();			
				File file=new File(destDir+zipEntry.getName());
				if(zipEntry.isDirectory()){
					file.mkdirs();
				}else{
					if(!file.getParentFile().exists()) file.getParentFile().mkdirs();					
					OutputStream outputStream=new FileOutputStream(file);					
					InputStream inputStream=zipFile.getInputStream(zipEntry);
					int c=0;
					while((c=inputStream.read(buf))!=-1){
						outputStream.write(buf, 0, c);
					}
					inputStream.close();
					outputStream.close();
				}					
			}
			zipFile.close();
		}catch (FileNotFoundException e) {					
			e.printStackTrace();
		}catch (IOException e) {					
			e.printStackTrace();
		}		
	}
	public void write(ZipOutputStream zos,File file,String arg1,boolean flag) throws IOException{    	
    	File[] list=file.listFiles();          	
    	if(file.isDirectory()){
    		if(list.length==0){
    			zos.putNextEntry(new ZipEntry(file.getName()+File.separator));
    			zos.closeEntry();
        	}{
        		arg1=arg1+File.separator;    	
        		for(int i=0;i<list.length;i++){
        			this.write(zos, list[i], arg1+list[i].getName(),flag);
        		}
        	}               	
    	}else{
    		byte[] buf=new byte[2048];
    		int c=0;
    		zos.putNextEntry(new ZipEntry(arg1));					
			FileInputStream fileInputStream=new FileInputStream(file);
			BufferedInputStream bufferedInputStream=new BufferedInputStream(fileInputStream);				
			while((c=bufferedInputStream.read(buf))!=-1){
				zos.write(buf,0,c);
			}
			zos.closeEntry();
			bufferedInputStream.close();				
    	}	
    	if(flag) file.delete();
    }
}
