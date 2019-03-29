package com.jbosframework.core.io;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import com.jbosframework.core.io.Resource;
/**
 * AbstractResource
 * @author youfu.wang
 * @version 1.0
 */
public class AbstractResource implements Resource{

	
	public boolean exists() throws IOException {	
		File file=this.getFile();
		if(file==null) return false;
		if(file.exists()) return true;
		return false;
	}

	
	public File getFile() throws IOException {
		return null;
	}

	
	public String getFileName() throws IOException{
		return null;
	}

	
	public InputStream getInputStream() throws IOException {
		return null;
	}

	
	public URI getURI() throws IOException {
		return null;
	}

	
	public URL getURL() throws IOException {
		return null;
	}

}
