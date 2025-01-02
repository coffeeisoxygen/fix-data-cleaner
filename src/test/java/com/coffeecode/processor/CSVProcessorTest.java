package com.coffeecode.processor;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    void testValidateFile() throws CustomException {
        assertTrue(processor.validateFile(testFile));
    }

    @Test
    void testReadCSV() throws CustomException {
        processor.validateFile(testFile);
        List<List<String>> data = processor.readToArray();
        assertNotNull(data);
        assertFalse(data.isEmpty());
        
        // Check metadata rows
        assertEquals("Transfer Balance Report", data.get(0).get(0));
        
        // Check header position (row 14)
        List<String> headers = data.get(14);
        assertTrue(headers.contains("DateTime"));
        assertTrue(headers.contains("Transaction ID"));
        
        // Check data rows
        List<String> firstDataRow = data.get(15);
        assertNotNull(firstDataRow);
        assertEquals(31, firstDataRow.size()); // Number of columns
    }

    @Test
    void testIterator() throws CustomException {
        processor.validateFile(testFile);
        Iterator<List<String>> iterator = processor.getIterator();
        assertTrue(iterator.hasNext());
        
        List<String> firstRow = iterator.next();
        assertNotNull(firstRow);
        assertTrue(firstRow.get(0).contains("Transfer Balance Report"));
    }

    @Test
    void testMetrics() throws CustomException {
        processor.validateFile(testFile);
        processor.readToArray();
        
        assertTrue(processor.getRowCount() > 0);
        assertTrue(processor.getMemoryUsed() > 0);
    }
}