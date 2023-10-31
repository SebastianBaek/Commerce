package com.example.commerce.exception;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AbstractException.class)
  public ResponseEntity<?> handleCustomException(AbstractException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .statusCode(e.getStatusCode())
        .message(e.getMessage())
        .build();

    return new ResponseEntity<>(errorResponse,
        Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode())));
  }
}
