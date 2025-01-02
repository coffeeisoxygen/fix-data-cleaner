package com.coffeecode.parser.csv;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.exception.CustomException;
import com.coffeecode.model.DataContainer;
import com.coffeecode.parser.csv.config.CSVConfig;

class CSVParserIntegrationTest {
    private static final String BASE_PATH = "src/test/resources/csv";
    private CSVParser parser;
    private CSVConfig config;

    @BeforeEach
    @SuppressWarnings("unused")
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
        verifyFileProcessing("1336_transfer_30_dec_24.csv", "Transfer Balance Report");
    }

    @Test
    void testParseKomisiFile() throws CustomException {
        verifyFileProcessing("1336_komisi_30_dec_24.csv", "Commission and Incentive Details Report");
    }

    @Test
    void testParseTransaksiFile() throws CustomException {
        verifyFileProcessing("1336_transaksi_30_dec_24.csv", "Transaction Detail Report");
    }

    private void verifyFileProcessing(String fileName, String expectedTitle) throws CustomException {
        // Setup
        Path path = Paths.get(BASE_PATH, fileName);
        parser.setPath(path);
        
        // Execute
        parser.parse();
        
        // Verify
        DataContainer result = parser.getResult();
        List<List<String>> metadata = result.getMetadata();
        
        assertAll(
            () -> assertNotNull(result.getHeaders(), "Headers should not be null"),
            () -> assertTrue(result.getHeaders().contains("DateTime"), "Should contain DateTime header"),
            () -> assertFalse(result.getContent().isEmpty(), "Content should not be empty"),
            () -> assertEquals(expectedTitle, metadata.get(0).get(0).replace("\"", ""), "Title should match")
        );
    }
}