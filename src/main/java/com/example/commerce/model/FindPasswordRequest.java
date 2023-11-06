package com.example.commerce.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
  @Size(max = 20)
  private String email;

  @NotBlank
  @Size(max = 15)
  private String username;

}
