package com.example.commerce.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RegisterUser {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Past
    private LocalDate birth;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;

    private List<String> role;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private String username;

    private LocalDateTime createdAt;

  }
}
