package com.app.invoice.exception;

public class DuplicateValueException extends RuntimeException {
    public DuplicateValueException(String message) {
        super(message);
    }

}
