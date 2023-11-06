package com.example.commerce.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
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
    @Size(max = 20)
    private String email;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    @Size(max = 20)
    private String password;

    @Past
    private LocalDate birth;

    @NotBlank
    @Size(max = 100)
    private String address;

    @NotBlank
    @Size(max = 11)
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
