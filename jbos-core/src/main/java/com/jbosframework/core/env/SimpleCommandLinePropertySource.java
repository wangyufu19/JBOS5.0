package com.jbosframework.core.env;

import com.jbosframework.core.Nullable;
import com.jbosframework.utils.StringUtils;

import java.util.List;

public class SimpleCommandLinePropertySource extends CommandLinePropertySource<CommandLineArgs> {
    public SimpleCommandLinePropertySource(String... args) {
        super((new SimpleCommandLineArgsParser()).parse(args));
    }

    public SimpleCommandLinePropertySource(String name, String[] args) {
        super(name, (new SimpleCommandLineArgsParser()).parse(args));
    }

    public String[] getPropertyNames() {
        return StringUtils.toStringArray(((CommandLineArgs)this.source).getOptionNames());
    }

    protected boolean containsOption(String name) {
        return ((CommandLineArgs)this.source).containsOption(name);
    }

    @Nullable
    protected List<String> getOptionValues(String name) {
        return ((CommandLineArgs)this.source).getOptionValues(name);
    }

    protected List<String> getNonOptionArgs() {
        return ((CommandLineArgs)this.source).getNonOptionArgs();
    }
}
