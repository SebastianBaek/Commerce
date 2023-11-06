package com.example.commerce.domain;

import javax.persistence.Column;
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
public class Product extends BaseEntity {

  @ManyToOne
  @JoinColumn
  private User user;

  @Column(nullable = false, unique = true)
  private String productName;

  @Column(nullable = false)
  private Long price;

  @Column(nullable = false)
  private Long amount;

  @Column(nullable = false)
  private String maker;

  @Column
  private Double rating;

  @Column
  private Long sales;
}
