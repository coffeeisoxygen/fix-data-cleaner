package com.coffeecode.processor;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.coffeecode.exception.CustomException;

public interface FileProcessor {
    boolean validateFile(File file) throws CustomException;
    Iterator<List<String>> getIterator() throws CustomException;
    List<List<String>> readToArray() throws CustomException;
    void close() throws CustomException;
    int getRowCount();
    long getMemoryUsed();
    FileType getFileType();  // Added public method
}
