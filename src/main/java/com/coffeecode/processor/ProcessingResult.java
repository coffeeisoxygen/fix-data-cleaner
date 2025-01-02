package com.coffeecode.processor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.coffeecode.model.DataContainer;
import com.coffeecode.validation.ValidationResult;

public class ProcessingResult {
    private final String filename;
    private final LocalDateTime processTime;
    private final DataContainer data;
    private final ValidationResult validationResult;
    private final List<String> messages;
    private ProcessingStatus status;

    public ProcessingResult(String filename, DataContainer data) {
        this.filename = filename;
        this.processTime = LocalDateTime.now();
        this.data = data;
        this.validationResult = new ValidationResult(0, 0);
        this.messages = new ArrayList<>();
        this.status = ProcessingStatus.STARTED;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void setStatus(ProcessingStatus status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return status == ProcessingStatus.COMPLETED && !validationResult.hasErrors();
    }

    public enum ProcessingStatus {
        STARTED, PARSING, VALIDATING, COMPLETED, FAILED
    }

    // Getters
    public String getFilename() { return filename; }
    public LocalDateTime getProcessTime() { return processTime; }
    public DataContainer getData() { return data; }
    public ValidationResult getValidationResult() { return validationResult; }
    public List<String> getMessages() { return new ArrayList<>(messages); }
    public ProcessingStatus getStatus() { return status; }
}