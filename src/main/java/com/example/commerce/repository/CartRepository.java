package com.example.commerce.repository;

import com.example.commerce.domain.Cart;
import com.example.commerce.domain.Product;
import com.example.commerce.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

  Optional<List<Cart>> findByUser(User user);

  Optional<Cart> findByUserAndProduct(User user, Product product);
}
