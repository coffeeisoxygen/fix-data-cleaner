package com.coffeecode.processor;

public enum FileType {
    CSV(".csv"),
    EXCEL(".xlsx", ".xls");

    private final String[] extensions;

    FileType(String... extensions) {
        this.extensions = extensions;
    }

    public String[] getExtensions() {
        return extensions;
    }
}