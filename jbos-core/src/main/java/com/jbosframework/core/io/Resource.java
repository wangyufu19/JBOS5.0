package com.jbosframework.core.io;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Resource
 * @author youfu.wang
 * @version 1.0
 */
public interface Resource {
	default ReadableByteChannel readableChannel() throws IOException {
		return Channels.newChannel(this.getInputStream());
	}
	default boolean isFile() {
		return false;
	}

	boolean exists() throws IOException ;

	InputStream getInputStream() throws IOException;

	File getFile() throws IOException;

	Resource createRelative(String var1) throws IOException;
	
	String getFileName() throws IOException;

	URI getURI() throws IOException;

	URL getURL() throws IOException;

	String getDescription();
}
