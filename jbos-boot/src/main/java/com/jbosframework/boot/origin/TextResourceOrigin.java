package com.jbosframework.boot.origin;

import com.jbosframework.core.io.Resource;
import com.jbosframework.utils.ObjectUtils;

public class TextResourceOrigin implements Origin {

    private final Resource resource;

    private final Location location;

    public TextResourceOrigin(Resource resource, Location location) {
        this.resource = resource;
        this.location = location;
    }

    /**
     * Return the resource where the property originated.
     * @return the text resource or {@code null}
     */
    public Resource getResource() {
        return this.resource;
    }

    /**
     * Return the location of the property within the source (if known).
     * @return the location or {@code null}
     */
    public Location getLocation() {
        return this.location;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof TextResourceOrigin) {
            TextResourceOrigin other = (TextResourceOrigin) obj;
            boolean result = true;
            result = result && ObjectUtils.nullSafeEquals(this.resource, other.resource);
            result = result && ObjectUtils.nullSafeEquals(this.location, other.location);
            return result;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ObjectUtils.nullSafeHashCode(this.resource);
        result = 31 * result + ObjectUtils.nullSafeHashCode(this.location);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append((this.resource != null) ? this.resource.getDescription() : "unknown resource [?]");
        if (this.location != null) {
            result.append(":").append(this.location);
        }
        return result.toString();
    }

    /**
     * A location (line and column number) within the resource.
     */
    public static final class Location {

        private final int line;

        private final int column;

        /**
         * Create a new {@link Location} instance.
         * @param line the line number (zero indexed)
         * @param column the column number (zero indexed)
         */
        public Location(int line, int column) {
            this.line = line;
            this.column = column;
        }

        /**
         * Return the line of the text resource where the property originated.
         * @return the line number (zero indexed)
         */
        public int getLine() {
            return this.line;
        }

        /**
         * Return the column of the text resource where the property originated.
         * @return the column number (zero indexed)
         */
        public int getColumn() {
            return this.column;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Location other = (Location) obj;
            boolean result = true;
            result = result && this.line == other.line;
            result = result && this.column == other.column;
            return result;
        }

        @Override
        public int hashCode() {
            return (31 * this.line) + this.column;
        }

        @Override
        public String toString() {
            return (this.line + 1) + ":" + (this.column + 1);
        }

    }

}
