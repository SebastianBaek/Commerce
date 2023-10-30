package com.example.commerce.exception.impl;

import com.example.commerce.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "유저가 존재하지 않습니다.";
  }
}