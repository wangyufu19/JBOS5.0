package com.jbosframework.boot.origin;

@FunctionalInterface
public interface OriginLookup<K> {

    /**
     * Return the origin of the given key or {@code null} if the origin cannot be
     * determined.
     * @param key the key to lookup
     * @return the origin of the key or {@code null}
     */
    Origin getOrigin(K key);

    /**
     * Attempt to lookup the origin from the given source. If the source is not a
     * {@link OriginLookup} or if an exception occurs during lookup then {@code null} is
     * returned.
     * @param source the source object
     * @param key the key to lookup
     * @param <K> the key type
     * @return an {@link Origin} or {@code null}
     */
    @SuppressWarnings("unchecked")
    static <K> Origin getOrigin(Object source, K key) {
        if (!(source instanceof OriginLookup)) {
            return null;
        }
        try {
            return ((OriginLookup<K>) source).getOrigin(key);
        }
        catch (Throwable ex) {
            return null;
        }
    }

}