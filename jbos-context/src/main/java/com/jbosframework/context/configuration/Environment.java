package com.jbosframework.context.configuration;
/**
 * Environment
 * @author youfu.wang
 * @version 1.0
 */
public class Environment {

	private String activeProfile="dev";

	public String getActiveProfile() {
		return activeProfile;
	}

	public void setActiveProfile(String activeProfile) {
		this.activeProfile = activeProfile;
	}
}
