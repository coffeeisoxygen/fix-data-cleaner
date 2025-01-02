package com.coffeecode.logger;

public class GeneralLogging extends AbstractLogging {

    public GeneralLogging(Class<?> clazz) {
        super(clazz);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }
}
