package com.example.finance2.controller;

import com.example.finance2.dto.ErrorResponse;
import com.example.finance2.exception.InsufficientFundsException;
import com.example.finance2.exception.InvalidAmountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Gör om fel till tydliga HTTP-statuskoder med ett felmeddelande i JSON
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidAmount(InvalidAmountException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse unreadableBody() {
        return new ErrorResponse("Ogiltigt belopp.");
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse insufficientFunds(InsufficientFundsException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
