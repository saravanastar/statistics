package com.n26.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class NotParsableDataException extends RuntimeException {
    public NotParsableDataException(String message) {
        super(message);
    }
    NotParsableDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}