package com.jbosframework.context;
import com.jbosframework.beans.factory.BeanFactory;
import com.jbosframework.core.Nullable;

/**
 * ApplicationContext
 * @author youfu.wang
 * @version 1.0
 */
public interface ApplicationContext extends BeanFactory,ApplicationEventPublisher {

	@Nullable
	String getId();

	String getApplicationName();

	String getDisplayName();

	long getStartupDate();

	@Nullable
	ApplicationContext getParent();

}
