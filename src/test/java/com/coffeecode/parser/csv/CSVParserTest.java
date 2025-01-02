package com.coffeecode.parser.csv;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
    void setUp() throws CustomException {
        MockitoAnnotations.openMocks(this);
        config = new CSVConfig.Builder().build();
        parser = new CSVParser(csvLibrary, config);
        container = new DataContainer();
        testFile = mock(File.class);
        when(testFile.exists()).thenReturn(true);
        when(testFile.getName()).thenReturn("test.csv");
        when(testFile.canRead()).thenReturn(true);
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
        CustomException mockError = new CustomException("Mock error");
        doThrow(mockError).when(csvLibrary).initialize(any(), any());

        CustomException thrown = assertThrows(
            CustomException.class, 
            () -> parser.parse(container)
        );
        
        assertEquals("Failed to parse CSV content", thrown.getMessage());
        assertEquals(mockError, thrown.getCause());
    }

    @Test
    void testInvalidFile() {
        when(testFile.exists()).thenReturn(false);
        assertFalse(parser.isValid());
    }

    @Test
    void testNullContainer() {
        assertThrows(CustomException.class, () -> parser.parse(null));
    }

    @Test
    void testReadError() throws CustomException {
        when(csvLibrary.readNext()).thenThrow(new CustomException("Read error"));
        assertThrows(CustomException.class, () -> parser.parse(container));
        verify(csvLibrary).close();
    }

    @Test
    void testInitializeError() throws CustomException {
        doThrow(new CustomException("Init error")).when(csvLibrary).initialize(any(), any());
        CustomException ex = assertThrows(CustomException.class, () -> parser.parse(container));
        assertEquals("Init error", ex.getMessage());
        verify(csvLibrary).close();
    }
}
