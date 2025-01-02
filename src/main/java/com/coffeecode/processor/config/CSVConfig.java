package com.coffeecode.processor.config;

public class CSVConfig extends AbstractProcessorConfig {
    private char separator = ',';
    private char quoteChar = '"';

    public static class Builder {
        private final CSVConfig config = new CSVConfig();

        public Builder charset(String charset) {
            config.charset = charset;
            return this;
        }

        public Builder separator(char separator) {
            config.separator = separator;
            return this;
        }

        public Builder maxRows(int maxRows) {
            config.maxRows = maxRows;
            return this;
        }

        public CSVConfig build() {
            return config;
        }
    }

    public char getSeparator() {
        return separator;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    @Override
    public long getMaxFileSize() {
        return maxFileSize;
    }
}