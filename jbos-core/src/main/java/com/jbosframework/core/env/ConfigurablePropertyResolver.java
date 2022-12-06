package com.jbosframework.core.env;

import com.jbosframework.core.Nullable;

public interface ConfigurablePropertyResolver extends PropertyResolver {


    void setPlaceholderPrefix(String var1);

    void setPlaceholderSuffix(String var1);

    void setValueSeparator(@Nullable String var1);

    void setIgnoreUnresolvableNestedPlaceholders(boolean var1);

    void setRequiredProperties(String... var1);

    void validateRequiredProperties() throws MissingRequiredPropertiesException;
}
