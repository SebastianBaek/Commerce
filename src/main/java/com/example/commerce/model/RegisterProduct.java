package com.example.commerce.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RegisterProduct {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotBlank
    @Size(max = 100)
    private String productName;

    @Min(10)
    private Long price;

    @Min(1)
    private Long amount;

    @NotBlank
    @Size(max = 20)
    private String maker;

  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private Long productId;

    private String productName;
  }
}
