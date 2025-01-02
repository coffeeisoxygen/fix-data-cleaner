package com.coffeecode.logger;

import java.io.File;

import com.coffeecode.exception.CustomException;
import com.coffeecode.exception.ThrowingRunnable;

public class TechnicalLogging extends AbstractLogging {

    public TechnicalLogging(Class<?> clazz) {
        super(clazz);
    }

    public void logExecutionTime(String taskName, ThrowingRunnable runnable) throws CustomException {
        long startTime = System.currentTimeMillis();
        try {
            runnable.run();
        } catch (Exception e) {
            log(LogLevel.ERROR, String.format("Task [%s] failed", taskName), e);
            throw new CustomException("Task execution failed", e);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log(LogLevel.DEBUG, String.format("Task [%s] executed in %d ms", taskName, duration));
        }
    }

    public void logSystemInfo() {
        log(LogLevel.DEBUG, String.format("OS: %s, Version: %s, Arch: %s",
            System.getProperty("os.name"),
            System.getProperty("os.version"),
            System.getProperty("os.arch")));
        log(LogLevel.DEBUG, String.format("Java: %s, VM: %s",
            System.getProperty("java.version"),
            System.getProperty("java.vm.name")));
        logMemoryUsage();
        logDiskUsage();
        logThreadDetails();
    }

    public void logPerformanceMetric(String category, String metric, Object value) {
        log(LogLevel.DEBUG, String.format("Performance[%s] - %s: %s", 
            category, metric, value));
    }

    public void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        logger.debug("Memory Usage: Total={} MB, Used={} MB, Free={} MB",
                runtime.totalMemory() / (1024 * 1024),
                (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024),
                runtime.freeMemory() / (1024 * 1024));
    }

    public void logThreadDetails() {
        Thread currentThread = Thread.currentThread();
        logger.debug("Thread Info: Name={}, ID={}, State={}",
                currentThread.getName(),
                currentThread.getId(),
                currentThread.getState());
    }

    public void logDiskUsage() {
        File diskPartition = new File("/");
        logger.debug("Disk Usage: Total={} GB, Used={} GB, Free={} GB",
                diskPartition.getTotalSpace() / (1024 * 1024 * 1024),
                (diskPartition.getTotalSpace() - diskPartition.getFreeSpace()) / (1024 * 1024 * 1024),
                diskPartition.getFreeSpace() / (1024 * 1024 * 1024));
    }

    public void logCustomMetric(String metricName, Object value) {
        logger.debug("Metric [{}]: {}", metricName, value);
    }

    public void logFileIOPerformance(String fileName, long fileSize, long startTime, long endTime) {
        double speed = (fileSize / 1024.0 / 1024.0) / ((endTime - startTime) / 1000.0);
        if (logger.isInfoEnabled()) {
            logger.info("File [{}]: Size={} MB, Duration={} ms, Speed={} MB/s",
                    fileName,
                    fileSize / (1024 * 1024),
                    endTime - startTime,
                    String.format("%.2f", speed));
        }
    }
}
