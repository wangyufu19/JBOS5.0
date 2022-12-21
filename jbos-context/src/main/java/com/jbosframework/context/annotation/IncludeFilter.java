package com.jbosframework.context.annotation;
import com.jbosframework.beans.annotation.Component;
import com.jbosframework.beans.annotation.Repository;
import com.jbosframework.beans.annotation.Service;
import com.jbosframework.beans.config.AnnotationMetadata;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * IncludeFilter
 * @author youfu.wang
 * @version 1.0
 */
public class IncludeFilter {
	public final static Class[] DEFAULT_FILTERS={Configuration.class,Component.class, Service.class, Repository.class};

	private Set<Class> include=new LinkedHashSet<>();

	public IncludeFilter(){
	}
	public void addFilter(Class filter){
		this.include.add(filter);
	}
	public boolean match(AnnotationMetadata metadata) {
		boolean match=false;
		if(metadata==null||include==null||include.size()<=0) {
			return false;
		}
		Class[] filters=include.toArray(new Class[0]);
		for(int i=0;i<filters.length;i++) {
			if(metadata.isAnnotation(filters[i])) {
				match=true;
				break;
			}
		}
		return match;
	}
}
