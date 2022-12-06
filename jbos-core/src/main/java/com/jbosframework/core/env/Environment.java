package com.jbosframework.core.env;
/**
 * Environment
 * @author youfu.wang
 * @version 1.0
 */
public interface Environment extends PropertyResolver{

	String[] getActiveProfiles();

	String[] getDefaultProfiles();

	/** @deprecated */
	@Deprecated
	boolean acceptsProfiles(String... var1);

	boolean acceptsProfiles(Profiles var1);
}
