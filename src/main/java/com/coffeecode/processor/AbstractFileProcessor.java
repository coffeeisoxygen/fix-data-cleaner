package com.coffeecode.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.coffeecode.exception.CustomException;
import com.coffeecode.logger.GeneralLogging;
import com.coffeecode.logger.TechnicalLogging;
import com.coffeecode.processor.config.ProcessorConfig;

public abstract class AbstractFileProcessor implements FileProcessor {
    protected final GeneralLogging logger;
    protected final TechnicalLogging techLogger;
    protected final ProcessorConfig config;
    protected File file;
    protected int rowsProcessed;
    protected long memoryUsed;
    protected List<List<String>> dataBuffer;

    protected AbstractFileProcessor(ProcessorConfig config) {
        this.config = config;
        this.logger = new GeneralLogging(this.getClass());
        this.techLogger = new TechnicalLogging(this.getClass());
        this.rowsProcessed = 0;
        this.dataBuffer = new ArrayList<>();
    }

    @Override
    public List<List<String>> readToArray() throws CustomException {
        return techLogger.logExecutionTime("Read File", () -> {
            initializeReader();
            Iterator<List<String>> iterator = getIterator();
            while (iterator.hasNext()) {
                dataBuffer.add(iterator.next());
                updateMetrics();
            }
            return new ArrayList<>(dataBuffer);
        });
    }

    @Override
    public boolean validateFile(File file) throws CustomException {
        return techLogger.logExecutionTime("Validate File", () -> {
            if (!file.exists()) {
                throw new CustomException("File not found", "FILE_NOT_FOUND");
            }
            if (!isValidExtension(file)) {
                throw new CustomException("Invalid file type", "INVALID_FILE_TYPE");
            }
            validateFileSize(file);
            this.file = file;
            return true;
        });
    }

    protected void validateFileSize(File file) throws CustomException {
        if (file.length() > config.getMaxFileSize()) {
            throw new CustomException("File too large", "FILE_SIZE_EXCEEDED");
        }
    }

    protected abstract boolean isValidExtension(File file);

    protected abstract void initializeReader() throws CustomException;

    protected void updateMetrics() throws CustomException {
        rowsProcessed++;
        if (rowsProcessed > config.getMaxRows()) {
            throw new CustomException("Max rows exceeded", "MAX_ROWS_EXCEEDED");
        }
        Runtime runtime = Runtime.getRuntime();
        memoryUsed = runtime.totalMemory() - runtime.freeMemory();
        techLogger.logPerformanceMetric("PROCESSING",
                String.format("Rows: %d, Memory: %dMB", rowsProcessed, memoryUsed / (1024 * 1024)), null);
    }

    @Override
    public void close() throws CustomException {
        techLogger.logExecutionTime("Close Reader", () -> {
            closeReader();
            logger.info("File processor closed");
            return null;
        });
    }

    @Override
    public int getRowCount() {
        return rowsProcessed;
    }

    @Override
    public long getMemoryUsed() {
        return memoryUsed;
    }

    protected abstract void closeReader() throws CustomException;

    // @Override
    // public abstract FileType getFileType(); // Changed to public
}