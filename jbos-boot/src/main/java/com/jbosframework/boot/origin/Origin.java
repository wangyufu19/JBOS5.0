package com.jbosframework.boot.origin;

public interface Origin {
    /**
     * Find the {@link Origin} that an object originated from. Checks if the source object
     * is an {@link OriginProvider} and also searches exception stacks.
     * @param source the source object or {@code null}
     * @return an optional {@link Origin}
     */
    static Origin from(Object source) {
        if (source instanceof Origin) {
            return (Origin) source;
        }
        Origin origin = null;
        if (source instanceof OriginProvider) {
            origin = ((OriginProvider) source).getOrigin();
        }
        if (origin == null && source instanceof Throwable) {
            return from(((Throwable) source).getCause());
        }
        return origin;
    }
}
