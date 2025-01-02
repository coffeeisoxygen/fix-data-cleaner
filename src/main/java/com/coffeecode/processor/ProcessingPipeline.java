package com.coffeecode.processor;

import java.nio.file.Path;

import com.coffeecode.exception.CustomException;
import com.coffeecode.model.DataContainer;
import com.coffeecode.parser.FileParser;
import com.coffeecode.validation.Validator;

public interface ProcessingPipeline {
    void process(Path inputPath) throws CustomException;
    void setParser(FileParser parser);
    void setValidator(Validator validator);
    DataContainer getResult();
}