package com.coffeecode.processor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Test
    void testProcessTransaksiFile() throws CustomException {
        // Setup additional validation rules
        validator.addRule(new ColumnRule(1, "Transaction Type", 
            value -> !value.trim().isEmpty()));
        validator.addRule(new ColumnRule(28, "Final transaction status", 
            value -> Arrays.asList("Completed", "Failed").contains(value)));
        
        // Process file
        Path path = Paths.get(BASE_PATH, "1336_transaksi_30_dec_24.csv");
        processor.process(path);
        
        // Verify results
        assertAll(
            () -> assertNotNull(processor.getResult()),
            () -> assertTrue(processor.getResult().getHeaders().contains("DateTime")),
            () -> assertTrue(processor.getResult().getHeaders().contains("Transaction Type")),
            () -> assertFalse(processor.getResult().getContent().isEmpty()),
            () -> assertEquals("Transaction Detail Report", 
                processor.getResult().getMetadata().get(0).get(0).replace("\"", ""))
        );
    }

    @Test
    void testProcessKomisiFile() throws CustomException {
        Path path = Paths.get(BASE_PATH, "1336_komisi_30_dec_24.csv");
        processor.process(path);
        
        assertAll(
            () -> assertNotNull(processor.getResult()),
            () -> assertTrue(processor.getResult().getHeaders().contains("DateTime")),
            () -> assertFalse(processor.getResult().getContent().isEmpty()),
            () -> assertTrue(processor.getResult().getMetadata().get(0).get(0)
                .contains("Commission and Incentive Details Report"))
        );
    }

    @Test
    void testValidationFailure() throws CustomException {
        // Add invalid date rule
        validator.addRule(new ColumnRule(0, "DateTime", 
            value -> value.matches("\\d{2}-\\d{2}-\\d{4}"))); // Wrong format
        
        Path path = Paths.get(BASE_PATH, "1336_transfer_30_dec_24.csv");
        assertThrows(CustomException.class, () -> processor.process(path));
    }
}