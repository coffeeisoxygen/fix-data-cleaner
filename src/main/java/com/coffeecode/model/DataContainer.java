package com.coffeecode.model;

import java.util.ArrayList;
import java.util.List;

public class DataContainer {
    private List<List<String>> rawData;
    private List<List<String>> metadata;
    private List<String> headers;
    private List<List<String>> content;
    
    public void addRawData(List<String> row) {
        if (rawData == null) rawData = new ArrayList<>();
        rawData.add(row);
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
    

}
