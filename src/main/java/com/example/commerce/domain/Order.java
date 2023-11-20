package com.example.commerce.domain;

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
public class Order extends BaseEntity {

  @ManyToOne
  @JoinColumn
  private User user;

  @ManyToOne
  @JoinColumn
  private Product product;

  private Long amount;

  private String address;

}
