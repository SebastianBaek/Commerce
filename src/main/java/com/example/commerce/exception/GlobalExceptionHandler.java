package com.example.commerce.exception;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handleCustomException(CustomException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .statusCode(e.getErrorCode().getHttpStatus().value())
        .message(e.getErrorCode().getMessage())
        .build();

    return new ResponseEntity<>(errorResponse,
        Objects.requireNonNull(HttpStatus.resolve(errorResponse.getStatusCode())));
  }
}
