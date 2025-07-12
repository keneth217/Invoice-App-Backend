package com.app.invoice.exception;

public class InvalidBusinessException extends RuntimeException{
    public InvalidBusinessException(String message) {
        super(message);
    }
    public InvalidBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
