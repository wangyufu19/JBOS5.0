package com.jbosframework.boot;

import com.jbosframework.core.env.Environment;

import java.io.PrintStream;

@FunctionalInterface
public interface Banner {


    void printBanner(Environment environment, Class<?> sourceClass, PrintStream out);

    /**
     * An enumeration of possible values for configuring the Banner.
     */
    enum Mode {

        /**
         * Disable printing of the banner.
         */
        OFF,

        /**
         * Print the banner to System.out.
         */
        CONSOLE,

        /**
         * Print the banner to the log file.
         */
        LOG

    }

}
