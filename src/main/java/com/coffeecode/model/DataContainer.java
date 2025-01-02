package com.coffeecode.model;

import java.util.ArrayList;
import java.util.List;

import com.coffeecode.exception.CustomException;

public class DataContainer {
    private List<List<String>> rawData;
    private List<List<String>> metadata;
    private List<String> headers;
    private List<List<String>> content;
    
    public DataContainer() {
        this.rawData = new ArrayList<>();
        this.metadata = new ArrayList<>();
        this.headers = new ArrayList<>();
        this.content = new ArrayList<>();
    }

    public void addRawData(List<String> row) throws CustomException {
        if (row == null) {
            throw new CustomException("Row cannot be null", "DATA_NULL_ROW");
        }
        rawData.add(new ArrayList<>(row));
    }

    public void setMetadataRange(int startRow, int endRow) throws CustomException {
        validateRange(startRow, endRow);
        metadata = new ArrayList<>(rawData.subList(startRow, endRow + 1));
    }

    public void setHeaderRow(int row) throws CustomException {
        if (row < 0 || row >= rawData.size()) {
            throw new CustomException("Invalid header row index", "DATA_INVALID_INDEX");
        }
        headers = new ArrayList<>(rawData.get(row));
    }

    public void setContentRange(int startRow, int endRow) throws CustomException {
        validateRange(startRow, endRow);
        content = new ArrayList<>(rawData.subList(startRow, endRow + 1));
    }

    private void validateRange(int startRow, int endRow) throws CustomException {
        if (startRow < 0 || endRow >= rawData.size() || startRow > endRow) {
            throw new CustomException("Invalid row range", "DATA_INVALID_RANGE");
        }
    }

    public List<List<String>> getRawData() {
        return new ArrayList<>(rawData);
    }

    public List<List<String>> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<List<String>> metadata) {
        this.metadata = metadata;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getContent() {
        return content;
    }

    public void setContent(List<List<String>> content) {
        this.content = content;
    }

    public boolean isEmpty() {
        return rawData.isEmpty();
    }

    public int size() {
        return rawData.size();
    }
}
