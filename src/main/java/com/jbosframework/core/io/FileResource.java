package com.jbosframework.core.io;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import com.jbosframework.core.io.AbstractResource;
/**
 * @author youfu.wang
 * @version 1.0
 */
public class FileResource extends AbstractResource{
	private File file=null;
	
	public FileResource(File file){		
		this.file=file;
	}
	public FileResource(String path){
		this.file=new File(path);
	}
	@Override
	public File getFile() throws IOException{
		return file;
	}

	@Override
	public String getFileName() throws IOException{
		return file.getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

	@Override
	public URI getURI() {
		return this.file.toURI();
	}

	@Override
	public URL getURL() throws IOException {
		return this.file.toURI().toURL();
	}

}
