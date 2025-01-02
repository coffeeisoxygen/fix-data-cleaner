package com.coffeecode.parser.csv;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.coffeecode.exception.CustomException;
import com.coffeecode.model.DataContainer;
import com.coffeecode.parser.csv.config.CSVConfig;

class CSVParserIntegrationTest {

    private static final String TEST_FILE = "src/test/resources/csv/1336_transfer_30_dec_24.csv";
    private static final int HEADER_ROW = 14;
    private static final int EXPECTED_COLUMNS = 31;
    private CSVParser parser;
    private DataContainer container;
    private Path testPath;

    @BeforeEach
    void setUp() throws CustomException {
        CSVConfig config = new CSVConfig.Builder()
                .charset("UTF-8")
                .separator(',')
                .quoteChar('"')
                .build();

        parser = new CSVParser(new OpenCSVLibrary(), config);
        container = new DataContainer();
        testPath = Paths.get(TEST_FILE);
        parser.setPath(testPath);
    }

    @Test
    void testReadActualFile() throws CustomException {
        // Parse file
        parser.parse(container);
        List<List<String>> data = container.getData();
        
        // Test file structure
        assertTrue(data.size() > HEADER_ROW, "File should have more rows than header position");
        
        // Test metadata
        assertMetadata(data);
        
        // Test headers
        assertHeaders(data.get(HEADER_ROW));
        
        // Test content
        assertContent(data.get(HEADER_ROW + 1));
    }
    
    private void assertMetadata(List<List<String>> data) {
        assertEquals("Transfer Balance Report", data.get(0).get(0).trim());
        assertEquals("End Date: 2024-12-30", data.get(2).get(0).trim());
        assertEquals("Start Date: 2024-12-30", data.get(4).get(0).trim());
    }
    
    private void assertHeaders(List<String> headers) {
        assertEquals(EXPECTED_COLUMNS, headers.size());
        assertEquals("DateTime", headers.get(0));
        assertEquals("Transaction ID", headers.get(1));
        assertEquals("Status Description", headers.get(30));
    }
    
    private void assertContent(List<String> firstRow) {
        assertEquals(EXPECTED_COLUMNS, firstRow.size());
        assertTrue(firstRow.get(0).contains("2024-12-30"));
        assertTrue(firstRow.get(29).equals("Completed"));
        assertTrue(firstRow.get(30).equals("Success"));
    }

    @Test
    void testValidateFile() {
        assertTrue(parser.isValid());
    }
}
