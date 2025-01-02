package com.coffeecode.processor;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.exception.CustomException;
import com.coffeecode.parser.csv.CSVParser;
import com.coffeecode.parser.csv.OpenCSVLibrary;
import com.coffeecode.parser.csv.config.CSVConfig;
import com.coffeecode.validation.ColumnRule;
import com.coffeecode.validation.ColumnValidator;

class DataProcessorTest {
    private static final String BASE_PATH = "src/test/resources/csv";
    private DataProcessor processor;
    private CSVParser parser;
    private ColumnValidator validator;

    @BeforeEach
    void setUp() {
        // Setup parser
        CSVConfig csvConfig = new CSVConfig.Builder()
                .charset("UTF-8")
                .separator(',')
                .quoteChar('"')
                .build();
        parser = new CSVParser(new OpenCSVLibrary(), csvConfig);
        
        // Setup validator
        validator = new ColumnValidator();
        validator.addRule(new ColumnRule(0, "DateTime", 
            value -> value.matches("\\d{4}-\\d{2}-\\d{2}")));
        
        // Setup processor
        processor = new DataProcessor();
        processor.setParser(parser);
        processor.setValidator(validator);
    }

    @Test
    void testProcessValidFile() throws CustomException {
        Path path = Paths.get(BASE_PATH, "1336_transfer_30_dec_24.csv");
        processor.process(path);
        
        assertNotNull(processor.getResult());
        assertTrue(processor.getResult().getHeaders().contains("DateTime"));
        assertFalse(processor.getResult().getContent().isEmpty());
    }

    @Test
    void testProcessWithoutConfig() {
        processor = new DataProcessor();
        Path path = Paths.get(BASE_PATH, "1336_transfer_30_dec_24.csv");
        
        CustomException exception = assertThrows(CustomException.class, 
            () -> processor.process(path));
        assertEquals("Invalid processor configuration", exception.getMessage());
    }
}