package com.app.invoice.exception;

public class PaymentExceedsBillException extends RuntimeException{
    public PaymentExceedsBillException(String message) {
        super(message);
    }
}
