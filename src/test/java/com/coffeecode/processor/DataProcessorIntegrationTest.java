package com.coffeecode.processor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coffeecode.exception.CustomException;
import com.coffeecode.model.DataContainer;
import com.coffeecode.parser.csv.CSVParser;
import com.coffeecode.parser.csv.OpenCSVLibrary;
import com.coffeecode.parser.csv.config.CSVConfig;
import com.coffeecode.validation.ColumnRule;
import com.coffeecode.validation.ColumnValidator;

class DataProcessorIntegrationTest {
    private static final String BASE_PATH = "src/test/resources/csv";
    private DataProcessor processor;
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorIntegrationTest.class);

    @BeforeEach
    void setUp() {
        // Setup CSV Parser with config
        CSVConfig csvConfig = new CSVConfig.Builder()
                .charset("UTF-8")
                .separator(',')
                .quoteChar('"')
                .build();
        CSVParser parser = new CSVParser(new OpenCSVLibrary(), csvConfig);
        
        // Setup Validator with enhanced rules
        ColumnValidator validator = new ColumnValidator();
        validator.addRule(new ColumnRule(0, "DateTime", 
            value -> value.matches("\\d{4}-\\d{2}-\\d{2}")));
        validator.addRule(new ColumnRule(1, "Transaction Type", 
            value -> !value.trim().isEmpty()));
        
        // Initialize Processor
        processor = new DataProcessor();
        processor.setParser(parser);
        processor.setValidator(validator);
        logger.info("Test setup completed");
    }

    @Test
    void testProcessAllFiles() throws CustomException {
        Map<String, FileValidation> fileTests = new LinkedHashMap<>();
        fileTests.put("1336_transfer_30_dec_24.csv", 
            new FileValidation("Transfer Balance Report", Arrays.asList("DateTime", "Amount")));
        fileTests.put("1336_komisi_30_dec_24.csv", 
            new FileValidation("Commission and Incentive Details Report", Arrays.asList("DateTime", "Transaction Type")));
        fileTests.put("1336_transaksi_30_dec_24.csv", 
            new FileValidation("Transaction Detail Report", Arrays.asList("DateTime", "Status")));

        for (Map.Entry<String, FileValidation> entry : fileTests.entrySet()) {
            logger.info("Processing file: {}", entry.getKey());
            Path path = Paths.get(BASE_PATH, entry.getKey());
            processor.process(path);
            
            FileValidation expected = entry.getValue();
            DataContainer result = processor.getResult();
            
            assertAll("File: " + entry.getKey(),
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.getHeaders().containsAll(expected.requiredHeaders), 
                    "Missing required headers"),
                () -> assertEquals(expected.title, 
                    result.getMetadata().get(0).get(0).replace("\"", ""),
                    "Title mismatch")
            );
            logger.info("Completed processing: {}", entry.getKey());
        }
    }

    private static class FileValidation {
        final String title;
        final List<String> requiredHeaders;

        FileValidation(String title, List<String> requiredHeaders) {
            this.title = title;
            this.requiredHeaders = requiredHeaders;
        }
    }
}