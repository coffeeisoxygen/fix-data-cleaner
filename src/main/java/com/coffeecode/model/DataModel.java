package com.coffeecode.model;

import java.util.List;

public interface DataModel {
    // Metadata operations
    void addMetadata(List<String> row);
    List<List<String>> getMetadata();
    
    // Header operations
    void setHeaders(List<String> headers);
    List<String> getHeaders();
    
    // Content operations
    void addContent(List<String> row);
    List<List<String>> getContent();
    
    // Legacy support
    void addRow(List<String> row);
    List<List<String>> getData();
}
