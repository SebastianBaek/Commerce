package com.example.commerce.exception.impl;

import com.example.commerce.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class EmailDoesNotAuthenticatedException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "이메일 인증 후 로그인 해주세요.";
  }
}