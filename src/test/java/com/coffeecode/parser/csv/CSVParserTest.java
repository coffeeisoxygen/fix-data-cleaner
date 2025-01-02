package com.coffeecode.parser.csv;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.exception.CustomException;
import com.coffeecode.model.DataContainer;
import com.coffeecode.parser.csv.config.CSVConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

class CSVParserIntegrationTest {

    private static final String BASE_PATH = "src/test/resources/csv";
    private CSVParser parser;
    private CSVConfig config;

    @BeforeEach
    void setUp() {
        config = new CSVConfig.Builder()
                .charset("UTF-8")
                .separator(',')
                .quoteChar('"')
                .build();
        parser = new CSVParser(new OpenCSVLibrary(), config);
    }

    @Test
    void testParseTransferFile() throws CustomException {
        // Setup
        Path path = Paths.get(BASE_PATH, "1336_transfer_30_dec_24.csv");
        parser.setPath(path);

        // Execute
        parser.parse();

        // Verify
        DataContainer result = parser.getResult();
        assertNotNull(result.getHeaders());
        assertTrue(result.getHeaders().contains("DateTime"));
        assertFalse(result.getContent().isEmpty());
    }
}
