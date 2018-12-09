package com.n26.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class InvalidStatDataException extends RuntimeException {
    public InvalidStatDataException(String message) {
        super(message);
    }
    InvalidStatDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
