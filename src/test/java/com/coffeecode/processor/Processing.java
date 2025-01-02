package com.coffeecode.processor;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.coffeecode.model.DataContainer;
import java.time.LocalDateTime;
import java.util.List;

class ProcessingResultTest {
    
    @Test
    void testResultLifecycle() {
        ProcessingResult result = new ProcessingResult("test.csv", new DataContainer());
        
        // Initial state
        assertEquals(ProcessingStatus.STARTED, result.getStatus());
        assertTrue(result.getMessages().isEmpty());
        assertFalse(result.isSuccess());
        
        // Transition through states
        result.addMessage("Started processing");
        result.setStatus(ProcessingStatus.PARSING);
        assertEquals(ProcessingStatus.PARSING, result.getStatus());
        
        result.addMessage("Parsing completed");
        result.setStatus(ProcessingStatus.VALIDATING);
        assertEquals(ProcessingStatus.VALIDATING, result.getStatus());
        
        result.addMessage("Validation completed");
        result.setStatus(ProcessingStatus.COMPLETED);
        assertTrue(result.isSuccess());
        assertEquals(3, result.getMessages().size());
    }
    
    @Test
    void testMessageImmutability() {
        ProcessingResult result = new ProcessingResult("test.csv", new DataContainer());
        result.addMessage("Original message");
        
        List<String> messages = result.getMessages();
        assertThrows(UnsupportedOperationException.class, 
            () -> messages.add("New message"));
    }
}
