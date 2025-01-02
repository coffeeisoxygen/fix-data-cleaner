package com.coffeecode.exception;

public class CustomException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String errorCode;

    public CustomException(String message) {
        super(message);
        this.errorCode = "UNKNOWN";
    }

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "UNKNOWN";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
