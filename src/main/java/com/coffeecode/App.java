package com.coffeecode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        // Test different log levels
        logger.trace("Trace Message!");
        logger.debug("Debug Message!");
        logger.info("Info Message!");
        logger.warn("Warn Message!");
        logger.error("Error Message!");

        // Test async logging with loop
        for (int i = 0; i < 1000; i++) {
            logger.info("Async logging test message #{}", i);
        }
    }
}
