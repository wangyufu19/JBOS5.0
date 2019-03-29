package com.jbosframework.web.mvc;
import java.util.Map;
import java.util.LinkedHashMap;
/**
 * ModelAndView
 * @author youfu.wang
 * @version 1.0
 */
public class ModelAndView {
	private Map<String,Object> modelObject=new LinkedHashMap<String,Object>();
	private String viewName;
	
	public void addModelObject(String key,Object value){
		if(!modelObject.containsKey(key)){
			modelObject.put(key, value);
		}
	}
	public Map<String,Object> getModelObjects(){
		return this.modelObject;
	}
	public Object getModelObject(String key){
		if(modelObject.containsKey(key)){
			return modelObject.get(key);
		}else
			return null;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	

}
