package com.coffeecode.exception;

@FunctionalInterface
public interface ThrowingCallable<T> {
    T call() throws CustomException;
}