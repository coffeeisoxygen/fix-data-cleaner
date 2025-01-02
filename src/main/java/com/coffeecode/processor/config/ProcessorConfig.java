package com.coffeecode.processor.config;

public interface ProcessorConfig {
    String getCharset();

    int getBufferSize();

    int getMaxRows();

    int getSkipLines();

    long getMaxFileSize();
}