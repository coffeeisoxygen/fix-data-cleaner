package com.coffeecode.processor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.exception.CustomException;
import com.coffeecode.parser.csv.CSVParser;
import com.coffeecode.parser.csv.OpenCSVLibrary;
import com.coffeecode.parser.csv.config.CSVConfig;
import com.coffeecode.validation.ColumnRule;
import com.coffeecode.validation.ColumnValidator;

class DataProcessorIntegrationTest {
    private static final String BASE_PATH = "src/test/resources/csv";
    private DataProcessor processor;

    @BeforeEach
    void setUp() {
        // Setup CSV Parser
        CSVConfig csvConfig = new CSVConfig.Builder()
                .charset("UTF-8")
                .separator(',')
                .quoteChar('"')
                .build();
        CSVParser parser = new CSVParser(new OpenCSVLibrary(), csvConfig);
        
        // Setup Validator with all rules
        ColumnValidator validator = new ColumnValidator();
        validator.addRule(new ColumnRule(0, "DateTime", 
            value -> value.matches("\\d{4}-\\d{2}-\\d{2}")));
        
        // Initialize Processor
        processor = new DataProcessor();
        processor.setParser(parser);
        processor.setValidator(validator);
    }

    @Test
    void testProcessAllFiles() throws CustomException {
        // Test file metadata
        Map<String, String> fileExpectations = Map.of(
            "1336_transfer_30_dec_24.csv", "Transfer Balance Report",
            "1336_komisi_30_dec_24.csv", "Commission and Incentive Details Report",
            "1336_transaksi_30_dec_24.csv", "Transaction Detail Report"
        );

        for (Map.Entry<String, String> entry : fileExpectations.entrySet()) {
            Path path = Paths.get(BASE_PATH, entry.getKey());
            processor.process(path);
            
            assertAll("File: " + entry.getKey(),
                () -> assertNotNull(processor.getResult()),
                () -> assertTrue(processor.getResult().getHeaders().contains("DateTime")),
                () -> assertFalse(processor.getResult().getContent().isEmpty()),
                () -> assertEquals(entry.getValue(), 
                    processor.getResult().getMetadata().get(0).get(0).replace("\"", ""))
            );
        }
    }
}