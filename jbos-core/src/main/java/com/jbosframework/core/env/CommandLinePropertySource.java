package com.jbosframework.core.env;


import com.jbosframework.core.Nullable;
import com.jbosframework.utils.StringUtils;

import java.util.List;

public abstract class CommandLinePropertySource<T> extends EnumerablePropertySource<T> {
    public static final String COMMAND_LINE_PROPERTY_SOURCE_NAME = "commandLineArgs";
    public static final String DEFAULT_NON_OPTION_ARGS_PROPERTY_NAME = "nonOptionArgs";
    private String nonOptionArgsPropertyName = "nonOptionArgs";

    public CommandLinePropertySource(T source) {
        super("commandLineArgs", source);
    }

    public CommandLinePropertySource(String name, T source) {
        super(name, source);
    }

    public void setNonOptionArgsPropertyName(String nonOptionArgsPropertyName) {
        this.nonOptionArgsPropertyName = nonOptionArgsPropertyName;
    }

    public final boolean containsProperty(String name) {
        if (this.nonOptionArgsPropertyName.equals(name)) {
            return !this.getNonOptionArgs().isEmpty();
        } else {
            return this.containsOption(name);
        }
    }

    @Nullable
    public final String getProperty(String name) {
        List optionValues;
        if (this.nonOptionArgsPropertyName.equals(name)) {
            optionValues = this.getNonOptionArgs();
            return optionValues.isEmpty() ? null : StringUtils.collectionToCommaDelimitedString(optionValues);
        } else {
            optionValues = this.getOptionValues(name);
            return optionValues == null ? null : StringUtils.collectionToCommaDelimitedString(optionValues);
        }
    }

    protected abstract boolean containsOption(String var1);

    @Nullable
    protected abstract List<String> getOptionValues(String var1);

    protected abstract List<String> getNonOptionArgs();
}
