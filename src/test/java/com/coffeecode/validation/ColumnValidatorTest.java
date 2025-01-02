package com.coffeecode.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ColumnValidatorTest {

    private ColumnValidator validator;
    private List<String> headers;
    private List<List<String>> content;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        validator = new ColumnValidator();
        headers = new ArrayList<>(Arrays.asList("DateTime", "Amount", "Status"));
        content = new ArrayList<>();
        content.add(new ArrayList<>(Arrays.asList("2024-01-02", "1000", "OK")));
        content.add(new ArrayList<>(Arrays.asList("2024-01-02", "2000", "PENDING")));
    }

    @Test
    void testColumnCountValidation() {
        // Valid case
        ValidationResult result = validator.validate(headers, content);
        assertFalse(result.hasErrors(), "Should pass with correct column count");

        // Invalid case
        content.add(Arrays.asList("2024-01-02", "1000")); // Missing column
        result = validator.validate(headers, content);
        assertTrue(result.hasErrors(), "Should fail with incorrect column count");
    }

    @Test
    void testDateFormatValidation() {
        validator.addRule(new ColumnRule(0, "DateTime",
                value -> value.matches("\\d{4}-\\d{2}-\\d{2}")));

        ValidationResult result = validator.validate(headers, content);
        assertFalse(result.hasErrors(), "Should pass with correct date format");

        content.add(new ArrayList<>(Arrays.asList("invalid-date", "1000", "OK")));
        result = validator.validate(headers, content);
        assertTrue(result.hasErrors(), "Should fail with incorrect date format");
    }
}
