package com.github.nazdov.slideshow.image.core.advice;

import com.github.nazdov.slideshow.image.core.exception.EntityNotFoundException;
import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class EntityNotFoundExceptionControllerAdvice {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> handleValidationException(EntityNotFoundException ex) {
        Map<String, String> errorDetails = Map.of("message", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
