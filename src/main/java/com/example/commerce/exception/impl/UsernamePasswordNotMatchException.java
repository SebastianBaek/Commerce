package com.example.commerce.exception.impl;

import com.example.commerce.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UsernamePasswordNotMatchException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "아이디 혹은 비밀번호가 일치하지 않습니다.";
  }
}