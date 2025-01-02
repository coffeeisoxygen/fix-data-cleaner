package com.coffeecode.processor;

import java.io.File;
import java.util.Iterator;
import java.util.List;

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
    void testReadToArray() throws CustomException {
        processor.validateFile(testFile);
        List<List<String>> data = processor.readToArray();
        assertNotNull(data);
        assertFalse(data.isEmpty());
    }

    @Test
    void testIterator() throws CustomException {
        processor.validateFile(testFile);
        Iterator<List<String>> iterator = processor.getIterator();
        assertTrue(iterator.hasNext());
        List<String> firstRow = iterator.next();
        assertNotNull(firstRow);
        assertFalse(firstRow.isEmpty());
    }

    @Test
    void testMemoryUsage() throws CustomException {
        processor.validateFile(testFile);
        processor.readToArray();
        assertTrue(processor.getMemoryUsed() > 0);
        assertTrue(processor.getRowCount() > 0);
    }
}