package com.example.commerce.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindPasswordRequest {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String username;

}
