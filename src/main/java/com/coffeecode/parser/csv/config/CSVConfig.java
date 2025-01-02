package com.coffeecode.parser.csv.config;

public class CSVConfig {

    private String charset = "UTF-8";
    private char separator = ',';
    private char quoteChar = '"';
    private int skipLines = 0;
    private int bufferSize = 8192;
    private boolean ignoreEmptyLines = true;
    private String headerMarker = "DateTime";

    private CSVConfig() {
    }

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

        public Builder quoteChar(char quoteChar) {
            config.quoteChar = quoteChar;
            return this;
        }

        public Builder skipLines(int skipLines) {
            config.skipLines = skipLines;
            return this;
        }

        public Builder bufferSize(int bufferSize) {
            config.bufferSize = bufferSize;
            return this;
        }

        public Builder ignoreEmptyLines(boolean ignore) {
            config.ignoreEmptyLines = ignore;
            return this;
        }

        public Builder headerMarker(String headerMarker) {
            config.headerMarker = headerMarker;
            return this;
        }

        public CSVConfig build() {
            return config;
        }
    }

    // Getters
    public String getCharset() {
        return charset;
    }

    public char getSeparator() {
        return separator;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public int getSkipLines() {
        return skipLines;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public boolean isIgnoreEmptyLines() {
        return ignoreEmptyLines;
    }

    public String getHeaderMarker() {
        return headerMarker;
    }
}
