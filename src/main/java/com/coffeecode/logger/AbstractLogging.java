package com.coffeecode.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLogging {
    protected final Logger logger;

    protected AbstractLogging(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    // Base logging methods
    protected void log(LogLevel level, String message) {
        switch (level) {
            case INFO -> logger.info(message);
            case DEBUG -> logger.debug(message);
            case ERROR -> logger.error(message);
            case WARN -> logger.warn(message);
        }
    }

    protected void log(LogLevel level, String message, Throwable throwable) {
        switch (level) {
            case ERROR -> logger.error(message, throwable);
            case WARN -> logger.warn(message, throwable);
            default -> logger.debug(message, throwable);
        }
    }
}
