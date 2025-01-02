package com.coffeecode.parser.csv;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.coffeecode.exception.CustomException;
import com.coffeecode.parser.csv.config.CSVConfig;

class OpenCSVLibraryTest {

    @TempDir
    Path tempDir;

    private OpenCSVLibrary csvLibrary;
    private CSVConfig config;
    private Path testFile;

    @BeforeEach
    void setUp() throws Exception {
        csvLibrary = new OpenCSVLibrary();
        config = new CSVConfig.Builder()
                .charset("UTF-8")
                .separator(',')
                .quoteChar('"')
                .build();

        testFile = tempDir.resolve("test.csv");
        Files.writeString(testFile,
                "Title,Value\n"
                + "Report,Balance\n"
                + "\n"
                + "DateTime,Amount,Status\n"
                + "2024-01-02,1000,OK\n"
        );
    }

    @Test
    void testInitialize() throws CustomException {
        assertDoesNotThrow(() -> csvLibrary.initialize(testFile, config));
    }

    @Test
    void testReadNext() throws CustomException {
        csvLibrary.initialize(testFile, config);
        List<String> firstLine = csvLibrary.readNext();
        assertEquals(Arrays.asList("Title", "Value"), firstLine);
    }

    @Test
    void testIsHeaderLine() {
        assertTrue(csvLibrary.isHeaderLine(Arrays.asList("DateTime", "Amount")));
        assertFalse(csvLibrary.isHeaderLine(Arrays.asList("Title", "Value")));
    }

    @Test
    void testIsBlankLine() {
        assertTrue(csvLibrary.isBlankLine(Arrays.asList("", "", "")));
        assertTrue(csvLibrary.isBlankLine(null));
        assertFalse(csvLibrary.isBlankLine(Arrays.asList("Data", "", "Value")));
    }

    @Test
    void testClose() throws CustomException {
        csvLibrary.initialize(testFile, config);
        assertDoesNotThrow(() -> csvLibrary.close());
    }
}
