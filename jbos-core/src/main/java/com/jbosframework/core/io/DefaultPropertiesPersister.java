package com.jbosframework.core.io;


import java.io.*;
import java.util.Properties;

public class DefaultPropertiesPersister implements PropertiesPersister {
    public DefaultPropertiesPersister() {
    }

    public void load(Properties props, InputStream is) throws IOException {
        props.load(is);
    }

    public void load(Properties props, Reader reader) throws IOException {
        props.load(reader);
    }

    public void store(Properties props, OutputStream os, String header) throws IOException {
        props.store(os, header);
    }

    public void store(Properties props, Writer writer, String header) throws IOException {
        props.store(writer, header);
    }

    public void loadFromXml(Properties props, InputStream is) throws IOException {
        props.loadFromXML(is);
    }

    public void storeToXml(Properties props, OutputStream os, String header) throws IOException {
        props.storeToXML(os, header);
    }

    public void storeToXml(Properties props, OutputStream os, String header, String encoding) throws IOException {
        props.storeToXML(os, header, encoding);
    }
}