package com.app.invoice.exception;

public class NoProductInStockException extends RuntimeException {
    public NoProductInStockException(String message) {
        super(message);
    }
}
