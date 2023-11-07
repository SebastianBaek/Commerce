package com.example.commerce.repository;

import com.example.commerce.domain.Product;
import com.example.commerce.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

  boolean existsByProductName(String productName);

  Optional<Product> findByIdAndUser(Long id, User user);
}
