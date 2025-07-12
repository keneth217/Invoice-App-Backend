package com.app.invoice.exception;

public class WrongOldPasswordException extends RuntimeException {
    public WrongOldPasswordException(String message) {
        super(message);
    }
}
