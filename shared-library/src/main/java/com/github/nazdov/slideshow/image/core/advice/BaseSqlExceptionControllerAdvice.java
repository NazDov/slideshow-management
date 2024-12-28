package com.github.nazdov.slideshow.image.core.advice;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class BaseSqlExceptionControllerAdvice {

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<?> handleSqlException(DuplicateKeyException ex) {
        Map<String, String> errorDetails = Map.of("message", "SQL Exception: duplicate key in payload");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
