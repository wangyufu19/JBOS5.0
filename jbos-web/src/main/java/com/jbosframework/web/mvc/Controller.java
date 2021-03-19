package com.jbosframework.web.mvc;
import com.jbosframework.web.mvc.data.Representation;
/**
 * 
 * @author youfu.wang
 * @version 5.0
 */
public interface Controller {

	public ModelAndView handleRequest(Representation entity);
}
