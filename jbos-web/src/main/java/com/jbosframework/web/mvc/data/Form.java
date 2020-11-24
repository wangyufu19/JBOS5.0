package com.jbosframework.web.mvc.data;
import com.jbosframework.web.mvc.data.Represention;
/**
 * Form
 * @author youfu.wang
 * @version 1.0
 */
public class Form {

	private Represention represention;
	
	public Form(Represention represention){
		this.represention=represention;	
	}
	public Represention getRepresention(){
		return this.represention;
	}
	public String get(String name){
		return represention.getParameter(name,false);
	}
	public String get(String name,boolean decode){
		return represention.getParameter(name,decode);
	}
	public String[] getArray(String name){
		return represention.getParameterValues(name,false);
	}
	public String[] getArray(String name,boolean decode){
		return represention.getParameterValues(name,decode);
	}	
}
