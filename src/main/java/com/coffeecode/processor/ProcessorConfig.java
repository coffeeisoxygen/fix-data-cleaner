package com.coffeecode.processor;

import com.coffeecode.parser.FileParser;
import com.coffeecode.validation.Validator;

public class ProcessorConfig {
    private FileParser parser;
    private Validator validator;
    private String outputPath;

    public static class Builder {
        private ProcessorConfig config = new ProcessorConfig();

        public Builder parser(FileParser parser) {
            config.parser = parser;
            return this;
        }

        public Builder validator(Validator validator) {
            config.validator = validator;
            return this;
        }

        public Builder outputPath(String outputPath) {
            config.outputPath = outputPath;
            return this;
        }

        public ProcessorConfig build() {
            return config;
        }
    }

    public FileParser getParser() { return parser; }
    public Validator getValidator() { return validator; }
    public String getOutputPath() { return outputPath; }
}