package com.example.commerce.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다."),
  PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."),
  ALREADY_EXISTS_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
  EMAIL_IS_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일 인증 후 로그인 해주세요."),
  ALREADY_EXISTS_USER_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
  ALREADY_EXISTS_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 상품명입니다."),
  USERNAME_AND_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다."),
  ;

  private final HttpStatus httpStatus;

  private final String message;
}
