package com.coffeecode.model;

import java.util.ArrayList;
import java.util.List;

public class DataContainer implements DataModel {
    private List<List<String>> metadata;
    private List<String> headers;
    private List<List<String>> content;

    public DataContainer() {
        this.metadata = new ArrayList<>();
        this.content = new ArrayList<>();
    }

    @Override
    public void addMetadata(List<String> row) {
        if (row != null) {
            metadata.add(new ArrayList<>(row));
        }
    }

    @Override
    public List<List<String>> getMetadata() {
        return new ArrayList<>(metadata);
    }

    @Override
    public void setHeaders(List<String> headers) {
        this.headers = headers != null ? new ArrayList<>(headers) : null;
    }

    @Override
    public List<String> getHeaders() {
        return headers != null ? new ArrayList<>(headers) : null;
    }

    @Override
    public void addContent(List<String> row) {
        if (row != null) {
            content.add(new ArrayList<>(row));
        }
    }

    @Override
    public List<List<String>> getContent() {
        return new ArrayList<>(content);
    }

    // Legacy support
    @Override
    public void addRow(List<String> row) {
        addContent(row);
    }

    @Override
    public List<List<String>> getData() {
        return getContent();
    }
}