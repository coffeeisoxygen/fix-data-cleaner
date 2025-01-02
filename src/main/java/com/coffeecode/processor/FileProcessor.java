package com.coffeecode.processor;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public interface FileProcessor {
    boolean validateFile(File file);

    Iterator<List<String>> getIterator();

    List<List<String>> readToArray();
}
