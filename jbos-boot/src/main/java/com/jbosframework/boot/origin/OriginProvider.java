package com.jbosframework.boot.origin;

@FunctionalInterface
public interface OriginProvider {

    /**
     * Return the source origin or {@code null} if the origin is not known.
     * @return the origin or {@code null}
     */
    Origin getOrigin();

}
