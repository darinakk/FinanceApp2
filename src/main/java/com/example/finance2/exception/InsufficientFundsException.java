package com.example.finance2.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Uttaget misslyckades: Du har inte tillräckligt med pengar på kontot.");
    }
}
