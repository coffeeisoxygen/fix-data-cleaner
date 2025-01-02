package com.coffeecode.validation;

import java.util.ArrayList;
import java.util.List;

public class ColumnValidator implements Validator {
    private final List<ColumnRule> rules;
    
    public ColumnValidator() {
        this.rules = new ArrayList<>();
    }
    
    public void addRule(ColumnRule rule) {
        rules.add(rule);
    }

    @Override
    public ValidationResult validate(List<String> headers, List<List<String>> content) {
        ValidationResult result = new ValidationResult(headers.size(), 0);
        
        // Validate column count
        for (int i = 0; i < content.size(); i++) {
            List<String> row = content.get(i);
            if (row.size() != headers.size()) {
                result.addError(
                    String.format("Column count mismatch. Expected: %d, Found: %d", 
                    headers.size(), row.size()), 
                    i + 1
                );
                continue;
            }
            
            // Apply column rules
            for (ColumnRule rule : rules) {
                if (!rule.validate(row)) {
                    result.addError(
                        String.format("Validation failed for column %s at line %d", 
                        headers.get(rule.getColumnIndex()), i + 1),
                        i + 1
                    );
                }
            }
        }
        
        return result;
    }
}