package com.coffeecode.model;

import java.util.List;

public interface DataModel {
    void addRow(List<String> row);
    List<List<String>> getData();
    void setHeaders(List<String> headers);
    List<String> getHeaders();
}
