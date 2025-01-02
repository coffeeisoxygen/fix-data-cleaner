package com.coffeecode.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<ValidationError> errors;
    @SuppressWarnings("unused")
    private final int expectedColumns;
    @SuppressWarnings("unused")
    private final int actualColumns;

    public ValidationResult(int expectedColumns, int actualColumns) {
        this.errors = new ArrayList<>();
        this.expectedColumns = expectedColumns;
        this.actualColumns = actualColumns;
    }

    public void addError(String message, int lineNumber) {
        errors.add(new ValidationError(message, lineNumber));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ValidationError> getErrors() {
        return new ArrayList<>(errors);
    }
}