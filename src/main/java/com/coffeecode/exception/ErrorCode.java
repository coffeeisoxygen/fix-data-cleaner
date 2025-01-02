package com.coffeecode.exception;

public class ErrorCode {
    private ErrorCode() {
        throw new IllegalStateException("Constant class");
    }

    public static final String CSV_READ_ERROR = "CSV_READ_ERROR";
    public static final String CSV_INVALID_FILE = "CSV_INVALID_FILE";
    public static final String CSV_CLOSE_ERROR = "CSV_CLOSE_ERROR";
}
