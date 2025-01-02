package com.coffeecode.parser;

import java.nio.file.Path;

import com.coffeecode.exception.CustomException;
import com.coffeecode.model.DataContainer;

public interface FileParser {
    void setPath(Path path) throws CustomException;
    
    void parse() throws CustomException;
    
    DataContainer getResult();
    
    boolean isValid();
    
    void close() throws CustomException;
}
