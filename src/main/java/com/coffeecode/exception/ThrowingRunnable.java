package com.coffeecode.exception;

@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws CustomException;
}
