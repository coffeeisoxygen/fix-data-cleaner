package com.coffeecode.parser.csv;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.exception.CustomException;
import com.coffeecode.parser.csv.config.CSVConfig;

class OpenCSVLibraryIntegrationTest {

    private static final String TEST_FILE = "src/test/resources/csv/1336_transfer_30_dec_24.csv";
    private OpenCSVLibrary csvLibrary;
    private CSVConfig config;

    @BeforeEach
    void setUp() {
        csvLibrary = new OpenCSVLibrary();
        config = new CSVConfig.Builder()
                .charset("UTF-8")
                .separator(',')
                .quoteChar('"')
                .build();
    }

    @Test
    void testReadActualFile() throws CustomException {
        Path path = Paths.get(TEST_FILE);
        csvLibrary.initialize(path, config);

        try {
            // Read until header
            List<String> line;
            boolean headerFound = false;
            while ((line = csvLibrary.readNext()) != null && !headerFound) {
                if (csvLibrary.isHeaderLine(line)) {
                    headerFound = true;
                    assertTrue(line.contains("DateTime"), "Header should contain DateTime");
                }
            }
            assertTrue(headerFound, "Header should be found in file");

            // Read first data row
            line = csvLibrary.readNext();
            assertNotNull(line);
            assertTrue(line.get(0).contains("2024-12-30"), "Should contain correct date");
        } finally {
            csvLibrary.close();
        }
    }
}
