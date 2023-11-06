package com.example.commerce.repository;

import com.example.commerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

  boolean existsByProductName(String productName);
}
