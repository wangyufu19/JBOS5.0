package com.jbosframework.context;
/**
 * Environment
 * @author youfu.wang
 * @version 1.0
 */
public class Environment {

	private String activeProfiles="dev";

	public String getActiveProfiles() {
		return activeProfiles;
	}

	public void setActiveProfiles(String activeProfiles) {
		this.activeProfiles = activeProfiles;
	}
}
