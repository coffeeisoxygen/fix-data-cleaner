package com.coffeecode.validation;

import java.util.List;
import java.util.function.Predicate;

public class ColumnRule {
    private final int columnIndex;
    private final String columnName;
    private final Predicate<String> validator;
    
    public ColumnRule(int columnIndex, String columnName, Predicate<String> validator) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
        this.validator = validator;
    }
    
    public boolean validate(List<String> row) {
        if (columnIndex >= row.size()) {
            return false;
        }
        return validator.test(row.get(columnIndex));
    }
    
    public int getColumnIndex() {
        return columnIndex;
    }
    
    public String getColumnName() {
        return columnName;
    }
}