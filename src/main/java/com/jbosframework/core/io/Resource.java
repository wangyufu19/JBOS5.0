package com.jbosframework.core.io;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.net.URI;
import java.net.URL;
/**
 * Resource
 * @author youfu.wang
 * @version 1.0
 */
public interface Resource {
	public boolean exists() throws IOException ;

	public InputStream getInputStream() throws IOException;

	public File getFile() throws IOException;
	
	public String getFileName() throws IOException;

	public URI getURI() throws IOException;

	public URL getURL() throws IOException;

}
