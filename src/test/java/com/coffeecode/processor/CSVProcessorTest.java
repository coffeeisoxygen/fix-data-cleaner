package com.coffeecode.processor;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.exception.CustomException;
import com.coffeecode.processor.config.CSVConfig;

class CSVProcessorTest {
    private CSVProcessor processor;
    private File testFile;
    private static final String TEST_FILE_PATH = "src/main/resources/testfile/csv/1336_transfer_30_dec_24.csv";

    @BeforeEach
    void setUp() {
        CSVConfig config = new CSVConfig.Builder()
            .charset("UTF-8")
            .separator(',')
            .maxRows(1000)
            .build();
        processor = new CSVProcessor(config);
        testFile = new File(TEST_FILE_PATH);
    }

    @Test
    void testFileValidation() throws CustomException {
        assertTrue(processor.validateFile(testFile), "File should be valid");
        assertTrue(testFile.exists(), "Test file should exist");
    }

    @Test
    void testReadStructure() throws CustomException {
        processor.validateFile(testFile);
        List<List<String>> data = processor.readToArray();
        
        assertNotNull(data, "Data should not be null");
        assertTrue(data.size() >= 16, "Should have at least 16 rows");
        
        // Verify Report Title
        assertEquals("Transfer Balance Report", data.get(0).get(0).trim());
        
        // Verify Metadata
        assertEquals("End Date: 2024-12-30", data.get(2).get(0).trim());
        assertEquals("Start Date: 2024-12-30", data.get(4).get(0).trim());
        
        // Verify Headers (row 14)
        List<String> headers = data.get(14);
        assertEquals(31, headers.size(), "Should have 31 columns");
        assertEquals("DateTime", headers.get(0).trim());
        assertEquals("Transaction ID", headers.get(1).trim());
        assertEquals("Status Description", headers.get(30).trim());
    }

    @Test
    void testDataContent() throws CustomException {
        processor.validateFile(testFile);
        List<List<String>> data = processor.readToArray();
        
        // Test first data row (row 15)
        List<String> firstDataRow = data.get(15);
        assertEquals(31, firstDataRow.size(), "Data row should have 31 columns");
        assertTrue(firstDataRow.get(0).matches("\\d{4}-\\d{2}-\\d{2}.*"), "Should match date format");
        assertTrue(firstDataRow.get(1).matches("\\d+"), "Transaction ID should be numeric");
    }

    @Test
    void testMetrics() throws CustomException {
        processor.validateFile(testFile);
        processor.readToArray();
        
        assertTrue(processor.getRowCount() > 0, "Should have processed rows");
        assertTrue(processor.getMemoryUsed() > 0, "Should have used memory");
    }
}