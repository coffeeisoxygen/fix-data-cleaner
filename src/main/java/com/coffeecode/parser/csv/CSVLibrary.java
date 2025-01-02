package com.coffeecode.parser.csv;

import java.io.File;
import java.util.List;

import com.coffeecode.exception.CustomException;
import com.coffeecode.parser.csv.config.CSVConfig;

public interface CSVLibrary {
    void initialize(File file, CSVConfig config) throws CustomException;

    List<String> readNext() throws CustomException;

    void close() throws CustomException;
}
