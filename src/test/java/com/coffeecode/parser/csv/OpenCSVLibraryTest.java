package com.coffeecode.parser.csv;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.exception.CustomException;
import com.coffeecode.parser.csv.config.CSVConfig;

class OpenCSVLibraryTest {

    private static final String BASE_PATH = "src/test/resources/csv";
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
    void testReadTransferFile() throws CustomException {
        Path path = Paths.get(BASE_PATH, "1336_transfer_30_dec_24.csv");
        csvLibrary.initialize(path, config);
        verifyHeaderAndContent("Transfer Balance Report");
    }

    @Test
    void testReadKomisiFile() throws CustomException {
        Path path = Paths.get(BASE_PATH, "1336_komisi_30_dec_24.csv");
        csvLibrary.initialize(path, config);
        verifyHeaderAndContent("Commission and Incentive Details Report");
    }

    private void verifyHeaderAndContent(String expectedTitle) throws CustomException {
        // Find first non-empty line
        List<String> line;
        while ((line = csvLibrary.readNext()) != null) {
            if (!csvLibrary.isBlankLine(line)) {
                assertEquals(expectedTitle, line.get(0).replace("\"", "").trim());
                break;
            }
        }

        // Find header
        boolean headerFound = false;
        while ((line = csvLibrary.readNext()) != null && !headerFound) {
            if (csvLibrary.isHeaderLine(line)) {
                headerFound = true;
                assertTrue(line.contains("DateTime"), "Header should contain DateTime");
            }
        }
        assertTrue(headerFound, "Header should be found");
    }
}
