package com.example.commerce.domain;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon extends BaseEntity {

  @ManyToOne
  @JoinColumn
  private User user;

  private Integer rate;

  private Integer saving;

  private LocalDate expiration;
}
