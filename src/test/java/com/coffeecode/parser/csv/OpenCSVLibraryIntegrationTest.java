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

class OpenCSVLibraryIntegrationTest {
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
    void testKomisiFile() throws CustomException {
        Path path = Paths.get(BASE_PATH, "1336_komisi_30_dec_24.csv");
        csvLibrary.initialize(path, config);
        processAndVerifyFile("Commission and Incentive Details Report");
    }

    @Test
    void testTransferFile() throws CustomException {
        Path path = Paths.get(BASE_PATH, "1336_transfer_30_dec_24.csv");
        csvLibrary.initialize(path, config);
        processAndVerifyFile("Transfer Balance Report");
    }

    @Test
    void testTransaksiFile() throws CustomException {
        Path path = Paths.get(BASE_PATH, "1336_transaksi_30_dec_24.csv");
        csvLibrary.initialize(path, config);
        processAndVerifyFile("Transaction Detail Report");
    }

    private void processAndVerifyFile(String expectedTitle) throws CustomException {
        try {
            // Skip empty lines until title found
            List<String> line;
            String firstCell = "";
            while ((line = csvLibrary.readNext()) != null) {
                if (!csvLibrary.isBlankLine(line)) {
                    firstCell = line.get(0)
                        .replace("\"", "")
                        .replace("\uFEFF", "") // Remove BOM
                        .trim();
                    if (!firstCell.isEmpty()) {
                        break;
                    }
                }
            }
            
            // Verify title
            assertEquals(expectedTitle, firstCell, 
                String.format("Title mismatch. Expected: %s, Found: %s", expectedTitle, firstCell));
            
            // Find header
            boolean headerFound = false;
            while ((line = csvLibrary.readNext()) != null && !headerFound) {
                if (!csvLibrary.isBlankLine(line) && csvLibrary.isHeaderLine(line)) {
                    headerFound = true;
                    assertTrue(line.contains("DateTime"), 
                        "Header must contain DateTime");
                }
            }
            assertTrue(headerFound, "Header not found");
        } finally {
            csvLibrary.close();
        }
    }
}
