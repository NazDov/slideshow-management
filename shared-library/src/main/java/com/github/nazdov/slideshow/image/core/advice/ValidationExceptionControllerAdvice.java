package com.github.nazdov.slideshow.image.core.advice;

import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ValidationExceptionControllerAdvice {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        Map<String, String> errorDetails = Map.of("message", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
