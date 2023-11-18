package com.example.commerce.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponInfo {

  private Integer rate;

  private Integer saving;

  private LocalDate expiration;

}
