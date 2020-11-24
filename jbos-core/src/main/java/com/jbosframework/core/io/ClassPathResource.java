package com.jbosframework.core.io;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import com.jbosframework.utils.JBOSClassloader;

/**
 * ClassPathResource
 * @author youfu.wang
 * @version 1.0
 */
public class ClassPathResource extends AbstractResource{
	private ClassLoader classLoader;
	private String path;
	
	public ClassPathResource(String path){		
		classLoader=JBOSClassloader.getClassLoader();	
		this.path=path;
	}

	@Override
	public File getFile() throws IOException {
		File file=null;
		file=new File(this.getURL().getFile());
		return file;
	}

	@Override
	public String getFileName() throws IOException {
		File file=this.getFile();
		if(file==null) return null;
		return file.getName();
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		InputStream is = null;
		is=classLoader.getResourceAsStream(this.path);
		if(is==null)
			throw new IOException("资源类库 [" + classLoader.getResource("")+path + "] 文件没有找到");
		return is;
	}

	@Override
	public URI getURI() throws IOException {
		URL url=null;
		url=classLoader.getResource(this.path);	
		if(url==null)
			throw new IOException("资源类库 [" + classLoader.getResource("")+path + "] 文件没有找到");
		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}		
		return null;
	}

	@Override
	public URL getURL() throws IOException {
		URL url=null;
		url=classLoader.getResource(this.path);
		if(url==null)
			throw new IOException("资源类库[" + path + "]文件没有找到");	
		return url;
	}

}
