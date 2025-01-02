package com.coffeecode.processor;

import java.nio.file.Path;

import com.coffeecode.exception.CustomException;
import com.coffeecode.logger.GeneralLogging;
import com.coffeecode.model.DataContainer;
import com.coffeecode.parser.FileParser;
import com.coffeecode.validation.ValidationResult;
import com.coffeecode.validation.Validator;

public class DataProcessor implements ProcessingPipeline {
    private static final String ERROR_PROCESS = "PROCESS_ERROR";
    
    private final ProcessorConfig config;
    private final GeneralLogging logger;
    private DataContainer result;

    public DataProcessor(ProcessorConfig config) {
        this.config = config;
        this.logger = new GeneralLogging(this.getClass());
    }

    @Override
    public void process(Path inputPath) throws CustomException {
        try {
            // Parse
            config.getParser().setPath(inputPath);
            config.getParser().parse();
            result = config.getParser().getResult();
            logger.info("Parsing completed for: " + inputPath.getFileName());

            // Validate
            ValidationResult validationResult = config.getValidator()
                .validate(result.getHeaders(), result.getContent());
            
            if (validationResult.hasErrors()) {
                throw new CustomException(
                    "Validation failed: " + validationResult.getErrors().size() + " errors found",
                    ERROR_PROCESS
                );
            }
            
            logger.info("Validation completed successfully");
            
        } catch (Exception e) {
            logger.error("Processing failed", e);
            throw new CustomException("Failed to process file", ERROR_PROCESS, e);
        }
    }

    @Override
    public DataContainer getResult() {
        return result;
    }

    @Override
    public void setParser(FileParser parser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setParser'");
    }

    @Override
    public void setValidator(Validator validator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setValidator'");
    }
}