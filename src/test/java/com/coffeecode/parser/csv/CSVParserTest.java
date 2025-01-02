package com.coffeecode.parser.csv;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.coffeecode.exception.CustomException;
import com.coffeecode.model.DataContainer;
import com.coffeecode.parser.csv.config.CSVConfig;

class CSVParserTest {

    @Mock
    private CSVLibrary csvLibrary;
    private CSVParser parser;
    private DataContainer container;
    private CSVConfig config;
    private File testFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        config = new CSVConfig.Builder().build();
        parser = new CSVParser(csvLibrary, config);
        container = new DataContainer();
        testFile = new File("test.csv");
        parser.setFile(testFile);
    }

    @Test
    void testValidFile() {
        assertTrue(parser.isValid());
    }

    @Test
    void testParseContent() throws CustomException {
        // Setup
        List<String> row1 = Arrays.asList("col1", "col2");
        List<String> row2 = Arrays.asList("val1", "val2");

        when(csvLibrary.readNext())
                .thenReturn(row1)
                .thenReturn(row2)
                .thenReturn(null);

        // Execute
        parser.parse(container);

        // Verify
        verify(csvLibrary).initialize(testFile, config);
        verify(csvLibrary, times(3)).readNext();
        verify(csvLibrary).close();

        List<List<String>> data = container.getData();
        assertEquals(2, data.size());
        assertEquals(row1, data.get(0));
        assertEquals(row2, data.get(1));
    }

    @Test
    void testParseError() throws CustomException {
        // Setup
        doThrow(new CustomException("Mock error"))
                .when(csvLibrary).initialize(any(), any());

        // Execute & Verify
        CustomException exception = assertThrows(CustomException.class, () -> parser.parse(container));
        assertEquals("Mock error", exception.getMessage());
    }
}
