package com.example.commerce.model;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoginUser {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private String username;

    private String token;

  }
}
