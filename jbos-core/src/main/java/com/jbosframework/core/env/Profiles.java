package com.jbosframework.core.env;

import java.util.function.Predicate;

public interface Profiles {
    boolean matches(Predicate<String> var1);

    static Profiles of(String... profiles) {
        return ProfilesParser.parse(profiles);
    }
}
