package com.coffeecode.parser;

import java.util.List;

import com.coffeecode.exception.CustomException;
import com.coffeecode.model.DataContainer;

public interface DataParser {
    void parse(DataContainer container) throws CustomException;

    int findHeaderPosition();

    void validateStructure();

    List<String> parseRow(String[] row);

    boolean isHeader(List<String> row);
}