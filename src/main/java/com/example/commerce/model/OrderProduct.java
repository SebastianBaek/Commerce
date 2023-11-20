package com.example.commerce.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderProduct {

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @Min(1)
    private Long productId;

    @Min(1)
    private Long amount;

    @NotBlank
    @Size(max = 100)
    private String address;

    private Long couponId;

  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private Long orderId;

    private String productName;

    private Long sum;

    private Long amount;

    private String address;
  }

}
