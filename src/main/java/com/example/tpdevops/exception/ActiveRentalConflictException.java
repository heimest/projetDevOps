package com.example.tpdevops.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ActiveRentalConflictException extends RuntimeException {
    public ActiveRentalConflictException(String message) {
        super(message);
    }
}
