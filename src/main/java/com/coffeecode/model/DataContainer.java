package com.coffeecode.model;

import java.util.ArrayList;
import java.util.List;

public class DataContainer implements DataModel {
    private List<List<String>> rawData;
    private List<String> headers;

    public DataContainer() {
        this.rawData = new ArrayList<>();
    }

    @Override
    public void addRow(List<String> row) {
        if (row != null) {
            rawData.add(new ArrayList<>(row));
        }
    }

    @Override
    public List<List<String>> getData() {
        return new ArrayList<>(rawData);
    }

    @Override
    public void setHeaders(List<String> headers) {
        this.headers = new ArrayList<>(headers);
    }

    @Override
    public List<String> getHeaders() {
        return headers != null ? new ArrayList<>(headers) : null;
    }
}