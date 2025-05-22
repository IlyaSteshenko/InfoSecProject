package com.informationsecurity.PasteBinService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class LoginAttemptsExceededException extends RuntimeException {
    public LoginAttemptsExceededException(String message) {
        super(message);
    }
}
