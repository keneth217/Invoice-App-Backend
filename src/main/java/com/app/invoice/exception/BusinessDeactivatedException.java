package com.app.invoice.exception;

public class BusinessDeactivatedException extends RuntimeException {
    public BusinessDeactivatedException(String message) {
        super(message);
    }
}
