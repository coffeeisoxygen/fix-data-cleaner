package com.coffeecode.model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataContainerTest {

    private DataContainer container;

    @BeforeEach
    void setUp() {
        container = new DataContainer();
    }

    @Test
    void testMetadataOperations() {
        List<String> row = Arrays.asList("Report Title", "Transfer Report");
        container.addMetadata(row);
        assertEquals(1, container.getMetadata().size());
        assertEquals(row, container.getMetadata().get(0));
    }

    @Test
    void testHeaderOperations() {
        List<String> headers = Arrays.asList("DateTime", "Transaction ID", "Amount");
        container.setHeaders(headers);
        assertEquals(headers, container.getHeaders());
    }

    @Test
    void testContentOperations() {
        List<String> row1 = Arrays.asList("2024-01-02", "TRX001", "1000");
        List<String> row2 = Arrays.asList("2024-01-02", "TRX002", "2000");
        container.addContent(row1);
        container.addContent(row2);
        assertEquals(2, container.getContent().size());
        assertEquals(row1, container.getContent().get(0));
    }

    @Test
    void testDefensiveCopying() {
        List<String> row = Arrays.asList("Data1", "Data2");
        container.addContent(row);
        row.set(0, "Modified");
        assertNotEquals("Modified", container.getContent().get(0).get(0));
    }

    @Test
    void testLegacySupport() {
        List<String> row = Arrays.asList("Legacy", "Data");
        container.addRow(row);
        assertEquals(row, container.getData().get(0));
    }
}
