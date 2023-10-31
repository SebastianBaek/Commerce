package com.example.commerce.exception;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handleCustomException(CustomException e) {
    log.error(e.getErrorCode().getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .statusCode(e.getErrorCode().getHttpStatus().value())
        .message(e.getErrorCode().getMessage())
        .build();

    return new ResponseEntity<>(errorResponse,
        Objects.requireNonNull(HttpStatus.resolve(errorResponse.getStatusCode())));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error("유효성 검증 실패");

    ErrorResponse errorResponse = ErrorResponse.builder()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .message("올바르지 않은 입력입니다.")
        .build();

    return new ResponseEntity<>(errorResponse,
        Objects.requireNonNull(HttpStatus.resolve(errorResponse.getStatusCode())));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error("입력 데이터 형식 오류 발생");

    ErrorResponse errorResponse = ErrorResponse.builder()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .message("지원하지 않는 데이터 형식의 입력입니다.")
        .build();

    return new ResponseEntity<>(errorResponse,
        Objects.requireNonNull(HttpStatus.resolve(errorResponse.getStatusCode())));
  }
}
