package com.coffeecode.validation;

import java.util.List;

public interface Validator {
    ValidationResult validate(List<String> headers, List<List<String>> content);
}