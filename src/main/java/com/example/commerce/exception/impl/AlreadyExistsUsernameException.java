package com.example.commerce.exception.impl;

import com.example.commerce.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsUsernameException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "이미 존재하는 아이디입니다.";
  }
}
