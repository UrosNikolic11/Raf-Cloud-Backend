package com.raf.rs.webDomaci3.controller;

import com.raf.rs.webDomaci3.exception.BadRequestException;
import com.raf.rs.webDomaci3.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleCustomException(BadRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleCustomException(NotFoundException exception) {
        return ResponseEntity.notFound().build();
    }
}
