package com.coffeecode.parser;

import com.coffeecode.exception.CustomException;
import com.coffeecode.model.DataContainer;

public interface Parser {
    void parse(DataContainer container) throws CustomException;

    boolean isValid();

    ParserType getType();
}
