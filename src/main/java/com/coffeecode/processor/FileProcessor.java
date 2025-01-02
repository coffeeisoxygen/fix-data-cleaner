package com.coffeecode.processor;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.coffeecode.exception.CustomException;

public interface FileProcessor {
    boolean validateFile(File file);

    Iterator<List<String>> getIterator() throws CustomException;

    List<List<String>> readToArray();
}
