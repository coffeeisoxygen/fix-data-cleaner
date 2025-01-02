package com.coffeecode.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnicalLogging {
    private final Logger logger;

    public TechnicalLogging(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Log execution time of a task.
     */
    public void logExecutionTime(String taskName, Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        logger.debug("Task [{}] executed in {} ms", taskName, (endTime - startTime));
    }

    /**
     * Log memory usage.
     */
    public void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        logger.debug("Memory Usage: Total={} MB, Used={} MB, Free={} MB",
                totalMemory / (1024 * 1024),
                usedMemory / (1024 * 1024),
                freeMemory / (1024 * 1024));
    }

    /**
     * Log thread details.
     */
    public void logThreadDetails() {
        Thread currentThread = Thread.currentThread();
        logger.debug("Thread Info: Name={}, ID={}, State={}",
                currentThread.getName(),
                currentThread.getId(),
                currentThread.getState());
    }
}
