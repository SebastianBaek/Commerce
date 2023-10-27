package com.example.commerce.model;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

public class LoginUser {

  @Data
  public static class Request {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

  }

  @Data
  @Builder
  public static class Response {

    private String username;

    private String token;

  }
}
