package com.github.nazdov.slideshow.image.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "request data is missing or invalid")
public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
