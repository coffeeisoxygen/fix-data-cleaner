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
    void testWithTransferFile() throws CustomException {
        Path path = Paths.get(BASE_PATH, "1336_transfer_30_dec_24.csv");
        csvLibrary.initialize(path, config);

        // Skip metadata
        List<String> line;
        while ((line = csvLibrary.readNext()) != null) {
            if (csvLibrary.isHeaderLine(line)) {
                assertTrue(line.contains("DateTime"), "Header not found");
                break;
            }
        }
    }

    @Test
    void testWithKomisiFile() throws CustomException {
        Path path = Paths.get(BASE_PATH, "1336_komisi_30_dec_24.csv");
        csvLibrary.initialize(path, config);
        verifyFileStructure("Commission and Incentive Details Report");
    }

    private void verifyFileStructure(String expectedTitle) throws CustomException {
        List<String> firstLine = csvLibrary.readNext();
        assertEquals(expectedTitle, firstLine.get(0).replace("\"", ""));
    }
}
