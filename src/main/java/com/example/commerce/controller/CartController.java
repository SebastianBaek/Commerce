package com.example.commerce.controller;

import com.example.commerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

  private final CartService cartService;

  @PostMapping("/add/{id}")
  @PreAuthorize("hasRole('COMMON')")
  public ResponseEntity<?> addProduct(
      @PathVariable Long id, Authentication authentication) {
    cartService.addProduct(id, authentication.getName());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/get")
  @PreAuthorize("hasRole('COMMON')")
  public ResponseEntity<?> getProducts(Authentication authentication) {
    return ResponseEntity.ok(cartService.getProducts(authentication.getName()));
  }

}
