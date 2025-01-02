package com.coffeecode.validation;

public class ValidationError {
    private final String message;
    private final int lineNumber;

    public ValidationError(String message, int lineNumber) {
        this.message = message;
        this.lineNumber = lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}