package com.coffeecode.processor.config;

public abstract class AbstractProcessorConfig implements ProcessorConfig {
    protected String charset = "UTF-8";
    protected int bufferSize = 8192;
    protected int maxRows = 10000;
    protected int skipLines = 0;
    protected long maxFileSize = 100 * 1024 * 1024; // 100MB

    @Override
    public String getCharset() {
        return charset;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public int getMaxRows() {
        return maxRows;
    }

    @Override
    public int getSkipLines() {
        return skipLines;
    }
}