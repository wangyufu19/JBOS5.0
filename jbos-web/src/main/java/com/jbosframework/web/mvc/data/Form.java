package com.jbosframework.web.mvc.data;

/**
 * Form
 * @author youfu.wang
 * @version 1.0
 */
public class Form {

	private Representation representation;
	
	public Form(Representation representation){
		this.representation=representation;
	}
	public Representation getRepresentation(){
		return this.representation;
	}
	public String get(String name){
		return representation.getParameter(name,false);
	}
	public String get(String name,boolean decode){
		return representation.getParameter(name,decode);
	}
	public String[] getArray(String name){
		return representation.getParameterValues(name,false);
	}
	public String[] getArray(String name,boolean decode){
		return representation.getParameterValues(name,decode);
	}	
}
