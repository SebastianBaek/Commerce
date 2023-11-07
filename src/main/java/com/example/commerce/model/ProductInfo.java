package com.example.commerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

  private String productName;

  private Long price;

  private Long amount;

  private String maker;

  private Double rating;

  private Long sales;
}
